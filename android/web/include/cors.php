<?php 
// Permitir solicitudes desde cualquier origen (*), o especificar el dominio permitido
header("Access-Control-Allow-Origin: *");

// Métodos permitidos (GET, POST, etc.)
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");

// Headers permitidos en la solicitud
header("Access-Control-Allow-Headers: Content-Type, Authorization");
?>