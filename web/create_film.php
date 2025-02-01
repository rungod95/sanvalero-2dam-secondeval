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
                    <h4>Create a new film</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-12 mb-4">
                    <label class="form-label" for="title">Title</label>
                    <input class="form-control" max="255" type="text" id="title" name="title" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="genre">Genre</label>
                    <input class="form-control" max="255" type="text" id="genre" name="genre" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="releaseDate">Release Date</label>
                    <input class="form-control" type="date" id="releaseDate" name="releaseDate" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="duration">Duration</label>
                    <input class="form-control" type="number" min="0" max="2147483646" id="duration" name="duration" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-check-label" for="viewed">Viewed</label>
                    <input class="form-check-input" type="checkbox" id="viewed" name="viewed" required>
                </div>
                <div class="col-12 mb-4">
                    <label class="form-label" for="director">Director</label>
                    <select id="director" class="form-select" required>
                        <option value="0" selected></option>
                    </select>
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
            title: document.querySelector('#title').value,
            genre: document.querySelector('#genre').value,
            releaseDate: document.querySelector('#releaseDate').value,
            duration: document.querySelector('#duration').value,
            viewed: document.querySelector('#viewed').checked,
            director: document.querySelector('#director').value
        }

        let validationError = false;

        Object.keys(formElements).forEach(key => {
            if (key !== 'viewed') {
                if (!formElements[key] || (key === 'director' && Number(formElements[key]) === 0)) {
                    validationError = true;
                    document.querySelector(`#${key}`).classList.add('is-invalid');
                } else {
                    document.querySelector(`#${key}`).classList.remove('is-invalid');
                }
            }
        });

        if (!validationError) {
            const film = {
                title: formElements.title,
                genre: formElements.genre,
                releaseDate: formElements.releaseDate,
                duration: formElements.duration,
                viewed: formElements.viewed,
                director: {
                    id: formElements.director
                }
            }

            fetch('http://localhost:8080/films', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(film)
            })
                .then(response => {
                    if (response.ok) {
                        window.alert('Film created successfully!');
                        document.querySelectorAll('input').forEach(input => input.value = '');
                        document.querySelector('#director').value = 0;
                    } else {
                        console.error('Error:', response.statusText);
                        window.alert('There was an error creating the film. Please try again later.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    window.alert('There was an error creating the film. Please try again later.');
                });
        } else {
            console.log('Validation error in form, please check the fields.');
        }
    })
    
    const directorsApi = fetch('http://localhost:8080/directors', { mode: "cors" })
        .then(response => response.json())
        .then(data => {
            data.forEach(director => {
                const directorSelect = document.querySelector('#director');
                const option = document.createElement('option');
                option.value = director.id;
                option.innerHTML = director.name + ' ' + director.lastName;
                directorSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            window.alert('There was an error retrieving directors to fill up the Director field. Please try again later.');
        });
</script>

</html>