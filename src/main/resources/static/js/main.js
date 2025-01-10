const API_URL = 'http://localhost:8080/api/v1/items';

// Initialize and auto-refresh
document.addEventListener('DOMContentLoaded', () => {
    loadData();
    setInterval(loadData, 30000);
});

async function loadData() {
    await Promise.all([loadItems(), loadAudit()]);
}

// Load and display items
async function loadItems() {
    try {
        const response = await fetch(`${API_URL}/report`);
        const items = await response.json();
        displayItems(items);
    } catch (error) {
        console.error('Error loading items:', error);
        document.getElementById('items-table').innerHTML = 
            '<tr><td colspan="6">Failed to load items</td></tr>';
    }
}

function displayItems(items) {
    const tbody = document.getElementById('items-table');
    tbody.innerHTML = items.length ? items.map(item => `
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>$${item.price.toFixed(2)}</td>
            <td>${item.quantity}</td>
            <td>${item.soldQuantity}</td>
            <td>
                <button onclick="updateItem(${item.id}, '${item.name}', ${item.price}, ${item.quantity})">Update</button>
                <button onclick="sellItem(${item.id}, ${item.quantity})">Sell</button>
                <button onclick="deleteItem(${item.id})" class="delete">Delete</button>
            </td>
        </tr>
    `).join('') : '<tr><td colspan="6">No items found</td></tr>';
}

// Load and display audit
async function loadAudit() {
    try {
        const response = await fetch(`${API_URL}/audit`);
        const audits = await response.json();
        displayAudit(audits);
    } catch (error) {
        console.error('Error loading audit:', error);
        document.getElementById('audit-container').innerHTML = 
            '<div class="error">Failed to load audit trail</div>';
    }
}

function displayAudit(audits) {
    const container = document.getElementById('audit-container');
    container.innerHTML = audits.length ? `
        <table>
            <thead>
                <tr>
                    <th>Time</th>
                    <th>Action</th>
                    <th>Item</th>
                    <th>Details</th>
                </tr>
            </thead>
            <tbody>
                ${audits.map(audit => `
                    <tr class="${audit.action.toLowerCase()}">
                        <td>${new Date(audit.timestamp).toLocaleString()}</td>
                        <td>${audit.action}</td>
                        <td>${audit.itemName}</td>
                        <td>${audit.description}</td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    ` : '<div class="empty">No audit records available</div>';
}

// Form handlers
document.getElementById('add-item-form').onsubmit = async (e) => {
    e.preventDefault();
    const form = e.target;
    
    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: form.querySelector('#item-name').value,
                price: parseFloat(form.querySelector('#item-price').value),
                quantity: parseInt(form.querySelector('#item-quantity').value)
            })
        });

        if (!response.ok) throw new Error('Failed to add item');
        form.reset();
        await loadData();
    } catch (error) {
        alert('Failed to add item');
    }
};

// Item actions
async function updateItem(id, name, price, quantity) {
    const newName = prompt('Enter new name:', name);
    const newPrice = prompt('Enter new price:', price);
    const newQuantity = prompt('Enter new quantity:', quantity);
    
    if (!newName || !newPrice || !newQuantity) return;
    
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: newName,
                price: parseFloat(newPrice),
                quantity: parseInt(newQuantity)
            })
        });

        if (!response.ok) throw new Error('Failed to update item');
        await loadData();
    } catch (error) {
        alert('Failed to update item');
    }
}

async function deleteItem(id) {
    if (!confirm('Delete this item?')) return;
    
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Failed to delete item');
        await loadData();
    } catch (error) {
        alert('Failed to delete item');
    }
}

async function sellItem(id, currentStock) {
    const quantity = prompt('Enter quantity to sell:', '1');
    if (!quantity) return;

    const amount = parseInt(quantity);
    if (isNaN(amount) || amount <= 0 || amount > currentStock) {
        alert(`Invalid quantity. Available: ${currentStock}`);
        return;
    }

    try {
        const response = await fetch(`${API_URL}/sell/${id}?quantity=${amount}`, {
            method: 'POST'
        });
        if (!response.ok) throw new Error('Failed to sell item');
        await loadData();
    } catch (error) {
        alert('Failed to sell item');
    }
}