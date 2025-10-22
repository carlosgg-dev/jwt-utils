document.addEventListener('DOMContentLoaded', () => {
    const encodeButton = document.getElementById('encode-button');
    const generateButton = document.getElementById('generate-button');
    const passwordInput = document.getElementById('password-input');
    const encodedPasswordElement = document.getElementById('encoded-password');
    const secretKeyEl = document.getElementById('secret-key');

    const API_BASE_URL = 'http://localhost:8080/api';

    const showError = (inputElement) => {
        inputElement.classList.add('error-input');
    };

    const clearError = (inputElement) => {
        inputElement.classList.remove('error-input');
    };

    passwordInput.addEventListener('input', () => {
        clearError(passwordInput);
    });

    encodeButton.addEventListener('click', async () => {
        const password = passwordInput.value;
        if (!password) {
            showError(passwordInput);
            return;
        }

        clearError(passwordInput);

        try {
            const response = await fetch(`${API_BASE_URL}/encode`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ password }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Network response was not ok');
            }

            const data = await response.json();
            encodedPasswordElement.textContent = data.encodedPassword;
        } catch (error) {
            console.error('Error encoding password:', error);
            encodedPasswordElement.textContent = `Error: ${error.message || 'Unknown error'}`;
            showError(passwordInput);
        }
    });

    generateButton.addEventListener('click', async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/generate`);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            secretKeyEl.textContent = data.secretKey;
        } catch (error) {
            console.error('Error generating secret key:', error);
            secretKeyEl.textContent = 'Error generating secret key.';
        }
    });
});