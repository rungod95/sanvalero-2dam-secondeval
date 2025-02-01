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
                </div>
            </template>
        </div>
    </main>
    <?php include 'include/footer.php'; ?>
</body>

<script>
    const template = document.querySelector('#card-template');
    const container = document.querySelector('#card-container');

    const filmsApi = fetch('http://localhost:8080/films', { mode: "cors" })
        .then(response => response.json())
        .then(data => {
            data.forEach(film => {
                const clone = template.content.cloneNode(true);
                clone.querySelector('.card-title').textContent = film.title;
                clone.querySelector('.card-director').textContent = film.director.name + ' ' + film.director.lastName;
                container.appendChild(clone);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = '<p>There was an error loading the films. Please try again later.</p>';
        });
</script>

</html>