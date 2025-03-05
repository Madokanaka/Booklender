<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ошибка</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">
<div class="container text-center">
    <div class="alert alert-danger" role="alert">
        <h4 class="alert-heading">Ошибка!</h4>
        <p>${errorMessage!"Что-то пошло не так. Попробуйте снова позже."}</p>
    </div>
    <a href="/profile" class="btn btn-primary">Вернуться в профиль</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
