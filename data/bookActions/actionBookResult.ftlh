<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Результат взятия книги</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1 class="my-4">Результат операции</h1>

    <#if isAuthorized>
        <#if message??>
            <div class="alert alert-success" role="alert">
                ${message}
            </div>
        </#if>
        <a href="/takeBooks" class="btn btn-secondary">Вернуться к списку книг</a>
        <a href="/profile" class="btn btn-primary">Перейти в профиль</a>
    <#else>
        <div class="alert alert-danger" role="alert">
            Вы не авторизованы. Пожалуйста, войдите в систему.
        </div>
        <a href="/login" class="btn btn-primary">Перейти на страницу входа</a>
    </#if>

</div>
</body>
</html>
