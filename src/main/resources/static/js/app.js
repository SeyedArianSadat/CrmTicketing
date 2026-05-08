// ========== تنظیمات اولیه ==========
const API_BASE = 'http://localhost:8080/api/v1';
let authToken = localStorage.getItem('authToken');

// ========== توابع لاگین ==========
async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (!username || !password) {
        showError('لطفاً نام کاربری و رمز عبور را وارد کنید');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        const result = await response.json();

        if (result.success && result.data) {
            authToken = result.data.accessToken;
            localStorage.setItem('authToken', authToken);
            window.location.href = '/dashboard';
        } else {
            showError(result.message || 'ورود ناموفق بود');
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('خطا در ارتباط با سرور');
    }
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
        setTimeout(() => errorDiv.style.display = 'none', 5000);
    } else {
        alert(message);
    }
}

// ========== توابع داشبورد ==========
async function loadDashboard() {
    if (!authToken) {
        window.location.href = '/login';
        return;
    }

    await loadCurrentUser();
    await loadUsers();
    await loadStats();
}

async function loadCurrentUser() {
    try {
        const response = await fetch(`${API_BASE}/users/me`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const result = await response.json();

        if (result.success && result.data) {
            document.getElementById('currentUser').innerHTML = `👤 ${result.data.username}`;
        }
    } catch (error) {
        console.error('Error loading current user:', error);
    }
}

async function loadUsers() {
    try {
        const response = await fetch(`${API_BASE}/users`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const result = await response.json();

        if (result.success && result.data) {
            displayUsers(result.data);
        } else if (result.statusCode === 401) {
            logout();
        }
    } catch (error) {
        console.error('Error loading users:', error);
        showError('خطا در بارگذاری کاربران');
    }
}

function displayUsers(users) {
    const tbody = document.getElementById('usersTableBody');

    if (!users || users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">هیچ کاربری یافت نشد</td></tr>';
        return;
    }

    tbody.innerHTML = users.map(user => `
        <tr data-username="${escapeHtml(user.username)}" data-email="${escapeHtml(user.email)}">
            <td>${user.id}</td>
            <td>${escapeHtml(user.username)}</td>
            <td>${escapeHtml(user.email)}</td>
            <td>${escapeHtml(user.fullName || user.username)}</td>
            <td>
                <span class="status-badge ${user.enabled ? 'status-active' : 'status-inactive'}">
                    ${user.enabled ? 'فعال' : 'غیرفعال'}
                </span>
            </td>
            <td>${user.roles.map(role => `<span class="role-badge">${role}</span>`).join('')}</td>
            <td class="action-buttons">
                ${!user.enabled ?
        `<button class="action-btn btn-enable" onclick="enableUser(${user.id})">فعال کردن</button>` :
        `<button class="action-btn btn-disable" onclick="disableUser(${user.id})">غیرفعال کردن</button>`
    }
            </td>
        </tr>
    `).join('');
}

async function loadStats() {
    try {
        const response = await fetch(`${API_BASE}/users`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const result = await response.json();

        if (result.success && result.data) {
            const users = result.data;
            document.getElementById('totalUsers').innerText = users.length;
            document.getElementById('activeUsers').innerText = users.filter(u => u.enabled).length;
            document.getElementById('disabledUsers').innerText = users.filter(u => !u.enabled).length;
        }
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

async function enableUser(userId) {
    if (!confirm('آیا می‌خواهید این کاربر را فعال کنید؟')) return;

    try {
        const response = await fetch(`${API_BASE}/users/${userId}/enable`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const result = await response.json();
        if (result.success) await refreshData();
        else showError(result.message || 'خطا در فعال کردن کاربر');
    } catch (error) {
        console.error('Error enabling user:', error);
        showError('خطا در ارتباط با سرور');
    }
}

async function disableUser(userId) {
    if (!confirm('آیا می‌خواهید این کاربر را غیرفعال کنید؟')) return;

    try {
        const response = await fetch(`${API_BASE}/users/${userId}/disable`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const result = await response.json();
        if (result.success) await refreshData();
        else showError(result.message || 'خطا در غیرفعال کردن کاربر');
    } catch (error) {
        console.error('Error disabling user:', error);
        showError('خطا در ارتباط با سرور');
    }
}

async function refreshData() {
    await loadUsers();
    await loadStats();
}

function filterTable() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const roleFilter = document.getElementById('roleFilter').value;
    const rows = document.querySelectorAll('#usersTableBody tr');

    rows.forEach(row => {
        if (row.cells && row.cells.length > 1) {
            const username = (row.getAttribute('data-username') || '').toLowerCase();
            const email = (row.getAttribute('data-email') || '').toLowerCase();
            const rolesText = row.innerText;

            let show = true;

            if (searchTerm) {
                if (!username.includes(searchTerm) && !email.includes(searchTerm)) {
                    show = false;
                }
            }

            if (roleFilter !== 'all') {
                if (!rolesText.includes(roleFilter)) {
                    show = false;
                }
            }

            row.style.display = show ? '' : 'none';
        }
    });
}

function logout() {
    localStorage.removeItem('authToken');
    window.location.href = '/login';
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
}

// ========== صفحه‌بندی ==========
if (window.location.pathname === '/dashboard' || window.location.pathname === '/dashboard.html') {
    document.addEventListener('DOMContentLoaded', loadDashboard);
}

if (window.location.pathname === '/login' || window.location.pathname === '/') {
    document.addEventListener('DOMContentLoaded', () => {
        const passwordInput = document.getElementById('password');
        if (passwordInput) {
            passwordInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') login();
            });
        }
    });
}