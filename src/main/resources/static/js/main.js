const apiUrl = 'http://localhost:8080/api/v1/items';

// Add Item
document.getElementById('add-item-form').onsubmit = async (e) => {
    e.preventDefault();
    const name = document.getElementById('item-name').value;
    const price = parseFloat(document.getElementById('item-price').value);
    const quantity = parseInt(document.getElementById('item-quantity').value);

    await submitData(apiUrl, 'POST', { name, price, quantity });

    alert('Item added!');
    document.getElementById('add-item-form').reset();
    fetchStockReport();
};

// Update Item
document.getElementById('update-item-form').onsubmit = async (e) => {
    e.preventDefault();

    const id = document.getElementById('update-item-id').value;
    const name = document.getElementById('update-item-name').value;
    const price = parseFloat(document.getElementById('update-item-price').value);
    const quantity = parseInt(document.getElementById('update-item-quantity').value);

    await submitData(`${apiUrl}/${id}`, 'PUT', { name, price, quantity });

    alert('Item updated successfully!');
    document.getElementById('update-item-form').style.display = 'none';
    fetchStockReport();
};

// Delete Item
async function deleteItem(id) {
    await fetch(`${apiUrl}/${id}`, { method: 'DELETE' });
    alert('Item deleted!');
    fetchStockReport();
}

// Fetch Stock Report
document.getElementById('fetch-stock-report').onclick = fetchStockReport;

async function fetchStockReport() {
    try {
        const response = await fetch(`${apiUrl}/report`);
        const items = await response.json();

        const tableBody = document.getElementById('stock-table-body');
        tableBody.innerHTML = '';

        items.forEach(item => {
            const row = `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${item.price}</td>
                    <td>${item.quantity}</td>
                    <td>${item.soldQuantity}</td>
                    <td>
                        <button class="btn-update" onclick="showUpdateForm(${item.id}, '${item.name}', ${item.price}, ${item.quantity})">Update</button>
                        <button class="btn-delete" onclick="deleteItem(${item.id})">Delete</button>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error fetching stock report:', error);
        alert('Failed to load stock report. Please try again later.');
    }
}

// Download Stock Report
document.getElementById('download-report').onclick = async () => {
    const items = await fetchData(`${apiUrl}/report`);
    downloadReport('stock_report.json', items);
};

// Show Update Form
function showUpdateForm(id, name, price, quantity) {
    document.getElementById('update-item-id').value = id;
    document.getElementById('update-item-name').value = name;
    document.getElementById('update-item-price').value = price;
    document.getElementById('update-item-quantity').value = quantity;
    document.getElementById('update-item-form').style.display = 'block';
}

// Automatically fetch stock report when the page loads
document.addEventListener("DOMContentLoaded", () => {
    fetchStockReport();
});

// Helper Functions - can be added to separate utils
async function fetchData(url) {
    const response = await fetch(url);
    return response.json();
}

async function submitData(url, method, data) {
    return await fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
}

function downloadReport(filename, data) {
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(data, null, 2));
    const anchor = document.createElement('a');
    anchor.href = dataStr;
    anchor.download = filename;
    anchor.click();
}