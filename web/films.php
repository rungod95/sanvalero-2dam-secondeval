<?php include 'include/cors.php'; ?>
<!DOCTYPE html>
<html lang="en">
<?php include 'include/head.php'; ?>
<body>
    <?php include 'include/header.php'; ?>
    <main>
        <div style="text-align: center; margin-top: 50px;">
            <section id="card-container">

            </section>
            <template id="card-template">
                <div class="card">
                    <img src="/images/film_placeholder.png" alt="Film Image" class="card-img">
                    <div class="card-content">
                        <h2 class="card-title"></h2>
                        <p class="card-director"></p>
                    </div>
                    <div class="card-delete">
                        <i class="bi bi-trash3-fill"></i>
                    </div>
                </div>
            </template>
        </div>
    </main>
    <?php include 'include/footer.php'; ?>
</body>

<script>
    const template = document.querySelector('#card-template');
    const container = document.querySelector('#card-container');

    function generateToken() {
        fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { 
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                username: "user",
                password: "password"
            }).toString()
        })
        .then(response => response.text())
        .then(data => {
            localStorage.setItem("jwtToken", data);  // Guardar el token
        })
        .catch(error => console.error("Error en login:", error));
    }

    generateToken();

    const filmsApi = fetch('http://localhost:8080/films', { mode: "cors" })
        .then(response => response.json())
        .then(data => {
            data.forEach(film => {
                const clone = template.content.cloneNode(true);
                clone.querySelector('.card').dataset.id = film.id;
                clone.querySelector('.card-title').textContent = film.title;
                clone.querySelector('.card-director').textContent = film.director.name + ' ' + film.director.lastName;
                container.appendChild(clone);
            });
            document.querySelectorAll('.card-delete').forEach(deleteButton => {
                deleteButton.addEventListener('click', () => {
                    if (window.confirm('Are you sure you want to delete this film?')) {
                        const filmId = deleteButton.parentElement.dataset.id;
                        fetch('http://localhost:8080/films/' + filmId, {
                            method: 'DELETE',
                            headers: {
                                'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
                            }
                        })
                            .then(response => {
                                if (response.ok) {
                                    window.alert('Film deleted successfully!');
                                    deleteButton.parentElement.remove();
                                } else {
                                    console.error('Error:', response.statusText);
                                    window.alert('There was an error deleting the film. Please try again later.');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                window.alert('There was an error deleting the film. Please try again later.');
                            });
                    }
                });
            });
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = '<p>There was an error loading the films. Please try again later.</p>';
        });
</script>

</html>