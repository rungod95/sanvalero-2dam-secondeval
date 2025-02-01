<?php include 'include/cors.php'; ?>
<!DOCTYPE html>
<html lang="en">
<?php include 'include/head.php'; ?>
<body>
    <?php include 'include/header.php'; ?>
    <main>
        <div id="form-container" class="container">
            <div class="row">
                <div class="col-12 mb-4">
                    <h4>Create a new director</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-12 mb-4">
                    <label class="form-label" for="name">Name</label>
                    <input class="form-control" max="255" type="text" id="name" name="name" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="lastName">Last Name</label>
                    <input class="form-control" max="255" type="text" id="lastName" name="lastName" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="birthDate">Birth Date</label>
                    <input class="form-control" type="date" id="birthDate" name="birthDate" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="nationality">Nationality</label>
                    <input class="form-control" max="255" type="text" id="nationality" name="nationality" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-check-label" for="awarded">Awarded</label>
                    <input class="form-check-input" type="checkbox" id="awarded" name="awarded" required>
                </div>
            </div>
            <div class="row mt-2">
                <div class="col-12">
                    <button id="submit-button" class="btn btn-primary">Create</button>
                </div>
            </div>
        </div>
    </main>
    <?php include 'include/footer.php'; ?>
</body>

<script>
    const submitButton = document.querySelector('#submit-button');
    submitButton.addEventListener('click', () => {
        // Validate form fields
        const formElements = {
            name: document.querySelector('#name').value,
            lastName: document.querySelector('#lastName').value,
            birthDate: document.querySelector('#birthDate').value,
            nationality: document.querySelector('#nationality').value,
            awarded: document.querySelector('#awarded').checked,
        }

        let validationError = false;

        Object.keys(formElements).forEach(key => {
            if (key !== 'awarded') {
                if (!formElements[key]) {
                    validationError = true;
                    document.querySelector(`#${key}`).classList.add('is-invalid');
                } else {
                    document.querySelector(`#${key}`).classList.remove('is-invalid');
                }
            }
        });

        if (!validationError) {
            const director = {
                name: formElements.name,
                lastName: formElements.lastName,
                birthDate: formElements.birthDate,
                nationality: formElements.nationality,
                awarded: formElements.awarded,
            }

            fetch('http://localhost:8080/directors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(director)
            })
                .then(response => {
                    if (response.ok) {
                        window.alert('Director created successfully!');
                        document.querySelectorAll('input').forEach(input => input.value = '');
                    } else {
                        console.error('Error:', response.statusText);
                        window.alert('There was an error creating the director. Please try again later.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    window.alert('There was an error creating the director. Please try again later.');
                });
        } else {
            console.log('Validation error in form, please check the fields.');
        }
    })
</script>

</html>