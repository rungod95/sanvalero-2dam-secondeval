<?php include 'include/cors.php'; ?>
<!DOCTYPE html>
<html lang="en">
<?php include 'include/head.php'; ?>
<body>
    <?php include 'include/header.php'; ?>
    <main>
        <div class="main-image-button-container">
            <div class="image-button-container">
                <a class="text-decoration-none text-reset image-button" href="films.php">
                    <img src="images/film_button.png" alt="Films">
                </a>
                <a href="create_film.php" class="text-decoration-none text-reset create-new-button">+ Create new</a>
            </div>
            <div class="image-button-container">
                <a class="text-decoration-none text-reset image-button" href="directors.php">
                    <img src="images/director_button.png" alt="Directors">
                </a>
                <a href="create_director.php" class="text-decoration-none text-reset create-new-button">+ Create new</a>
            </div>
        </div>
    </main>
    <?php include 'include/footer.php'; ?>
</body>
</html>