/**
 * SmartRoute AI - Authentication & User State Module
 */

const API_BASE_URL = (window.location.port ? `${window.location.protocol}//${window.location.hostname}:${window.location.port}/api` : 'http://localhost:8085/api');

let currentUser = null;
let currentAuthMode = 'login'; // 'login' or 'register'

// Initialize Auth State from LocalStorage
function initAuth() {
    const savedUser = localStorage.getItem('smartroute_user');
    if (savedUser) {
        try {
            currentUser = JSON.parse(savedUser);
            updateNavAuthUI();
        } catch (e) {
            localStorage.removeItem('smartroute_user');
        }
    }
}

function updateNavAuthUI() {
    const authNavContainer = document.getElementById('authNavContainer');
    const userProfileBadge = document.getElementById('userProfileBadge');
    const userAvatarText = document.getElementById('userAvatarText');
    const userNameLabel = document.getElementById('userNameLabel');

    if (currentUser) {
        if (authNavContainer) authNavContainer.classList.add('hidden');
        if (userProfileBadge) {
            userProfileBadge.classList.remove('hidden');
            userNameLabel.textContent = currentUser.name || 'User';
            userAvatarText.textContent = (currentUser.name || 'U').charAt(0).toUpperCase();
        }
    } else {
        if (authNavContainer) authNavContainer.classList.remove('hidden');
        if (userProfileBadge) userProfileBadge.classList.add('hidden');
    }
}

function openAuthModal(mode = 'login') {
    currentAuthMode = mode;
    const modal = document.getElementById('authModal');
    const title = document.getElementById('authModalTitle');
    const desc = document.getElementById('authModalDesc');
    const nameGroup = document.getElementById('nameFormGroup');
    const submitBtn = document.getElementById('authSubmitBtn');
    const toggleText = document.getElementById('authToggleText');
    const toggleLink = document.getElementById('authToggleLink');
    const errorMsg = document.getElementById('authErrorMsg');

    if (errorMsg) errorMsg.classList.add('hidden');

    if (mode === 'register') {
        title.textContent = 'Create SmartRoute Account';
        desc.textContent = 'Register to track your route searches and access optimization history.';
        nameGroup.classList.remove('hidden');
        submitBtn.innerHTML = '<i class="fa-solid fa-user-plus"></i> Register';
        toggleText.textContent = 'Already have an account?';
        toggleLink.textContent = 'Login';
    } else {
        title.textContent = 'Welcome to SmartRoute AI';
        desc.textContent = 'Login to save your route searches and access personalized history.';
        nameGroup.classList.add('hidden');
        submitBtn.innerHTML = '<i class="fa-solid fa-arrow-right-to-bracket"></i> Login';
        toggleText.textContent = "Don't have an account?";
        toggleLink.textContent = 'Register';
    }

    modal.classList.remove('hidden');
}

function closeAuthModal() {
    const modal = document.getElementById('authModal');
    if (modal) modal.classList.add('hidden');
}

function toggleAuthMode() {
    openAuthModal(currentAuthMode === 'login' ? 'register' : 'login');
}

function fillDemoCredentials() {
    document.getElementById('authEmail').value = 'demo@smartroute.ai';
    document.getElementById('authPassword').value = 'admin123';
}

async function handleAuthSubmit(event) {
    event.preventDefault();
    const email = document.getElementById('authEmail').value.trim();
    const password = document.getElementById('authPassword').value.trim();
    const name = document.getElementById('authName')?.value.trim();
    const errorMsg = document.getElementById('authErrorMsg');

    const endpoint = currentAuthMode === 'register' ? `${API_BASE_URL}/auth/register` : `${API_BASE_URL}/auth/login`;
    const payload = currentAuthMode === 'register' ? { name, email, password } : { email, password };

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await response.json();
        if (response.ok && data.success) {
            currentUser = {
                userId: data.userId,
                name: data.name,
                email: data.email,
                token: data.token
            };
            localStorage.setItem('smartroute_user', JSON.stringify(currentUser));
            updateNavAuthUI();
            closeAuthModal();
            if (typeof fetchRouteHistory === 'function') fetchRouteHistory();
        } else {
            errorMsg.textContent = data.message || 'Authentication failed. Please check credentials.';
            errorMsg.classList.remove('hidden');
        }
    } catch (err) {
        errorMsg.textContent = 'Could not reach backend server at ' + API_BASE_URL + '. Is Spring Boot running?';
        errorMsg.classList.remove('hidden');
    }
}

function openUserProfileModal() {
    if (!currentUser) return;
    document.getElementById('profileModalName').textContent = currentUser.name;
    document.getElementById('profileModalEmail').textContent = currentUser.email;
    document.getElementById('profileAvatarLarge').textContent = currentUser.name.charAt(0).toUpperCase();
    document.getElementById('profileModal').classList.remove('hidden');
}

function closeProfileModal() {
    document.getElementById('profileModal').classList.add('hidden');
}

function handleLogout() {
    currentUser = null;
    localStorage.removeItem('smartroute_user');
    updateNavAuthUI();
    closeProfileModal();
    if (typeof fetchRouteHistory === 'function') fetchRouteHistory();
}
