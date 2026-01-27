(function () {
    'use strict';

    const userInfoBody = document.getElementById('userInfoBody');

    async function fetchJson(url) {
        const res = await fetch(url, { method: 'GET' });
        if (!res.ok) {
            const text = await res.text().catch(() => '');
            throw new Error(text || (res.status + ' ' + res.statusText));
        }
        return res.json();
    }

    function roleNames(user) {
        return (user.roles || []).map(r => r.name).join(' ');
    }

    function renderUser(user) {
        userInfoBody.innerHTML = '';
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${user.id ?? ''}</td>
            <td>${user.firstName ?? ''}</td>
            <td>${user.lastName ?? ''}</td>
            <td>${user.age ?? ''}</td>
            <td>${user.email ?? ''}</td>
            <td>${roleNames(user)}</td>
        `;
        userInfoBody.appendChild(tr);
    }

    async function init() {
        if (!userInfoBody) {
            return;
        }
        const user = await fetchJson('/api/user');
        renderUser(user);
    }

    document.addEventListener('DOMContentLoaded', () => {
        init().catch(err => {
            console.error(err);
        });
    });
})();
