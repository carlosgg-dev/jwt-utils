document.addEventListener('DOMContentLoaded', () => {
    const encodeButton = document.getElementById('encode-button');
    const generateHS512Button = document.getElementById('generate-hs512-button');
    const generateECDSAP256Button = document.getElementById('generate-ecdsap256-button');
    const passwordInput = document.getElementById('password-input');
    const encodedPasswordOutput = document.getElementById('encoded-password');
    const secretHS512Output = document.getElementById('secret-hs512-key');
    const secretECDSAP256Output = document.getElementById('secret-ecdsap256-key');

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
        const password = passwordInput.value.trim();
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
            encodedPasswordOutput.textContent = data.encodedPassword;
        } catch (error) {
            console.error('Error encoding password:', error);
            encodedPasswordOutput.textContent = `Error: ${error.message || 'Unknown error'}`;
            showError(passwordInput);
        }
    });

    generateHS512Button.addEventListener('click', async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/generateHS512`);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            secretHS512Output.textContent = data.secretKey;
        } catch (error) {
            console.error('Error generating HS512 key:', error);
            secretHS512Output.textContent = 'Error generating HS512 key.';
        }
    });

    generateECDSAP256Button.addEventListener('click', async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/generateECDSAP256`);

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            secretECDSAP256Output.textContent = `Public Key: ${data.publicKey}\n\nPrivate Key: ${data.privateKey}`;
        } catch (error) {
            console.error('Error generating ECDSA P-256 key:', error);
            secretECDSAP256Output.textContent = 'Error generating ECDSA P-256 key.';
        }
    });
});