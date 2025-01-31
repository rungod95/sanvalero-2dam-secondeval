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
                    <img src="/images/director_placeholder.png" alt="Director Image" class="card-img">
                    <div class="card-content">
                        <h2 class="card-name"></h2>
                        <p class="card-birth-date"></p>
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

    const directorsApi = fetch('http://localhost:8080/directors', { mode: "cors" })
        .then(response => response.json())
        .then(data => {
            data.forEach(director => {
                const clone = template.content.cloneNode(true);
                clone.querySelector('.card-name').textContent = director.name + ' ' + director.lastName;
                clone.querySelector('.card-birth-date').textContent = director.birthDate;
                container.appendChild(clone);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = '<p>There was an error loading the directors. Please try again later.</p>';
        });
</script>

</html>