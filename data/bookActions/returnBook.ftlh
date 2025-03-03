<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Библиотека</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1 class="my-4">Возврат книг</h1>

    <#if isAuthorized>
        <#if hasBooks>
            <h2 class="mb-5">Ваши взятые книги</h2>
            <ul class="list-unstyled row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <ul class="list-unstyled row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                    <#list books as book>
                        <li class="col">
                            <div class="card h-100 shadow-sm text-center d-flex flex-column p-4">
                                <img src="${book.imagePath}" class="card-img-top img-fluid object-fit-cover" style="height: 500px;" alt="${book.title}">
                                <div class="card-body flex-grow-1">
                                    <h5 class="card-title">${book.title}</h5>
                                    <p class="card-text text-muted">Автор: ${book.author}</p>
                                </div>
                                <div class="mt-auto">
                                    <a href="/book/${book.id}" class="btn btn-primary">Подробнее</a>
                                    <a href="/returnBook/${book.id}" class="btn btn-danger ml-2">Вернуть книгу</a>
                                </div>
                            </div>
                        </li>
                    </#list>
                </ul>
            </ul>
        <#else>
            <p>У вас нет взятых книг для возврата.</p>
            <a href="/profile" class="btn btn-primary">Профиль</a>
        </#if>
    <#else>
        <div class="alert alert-danger" role="alert">
            Вы не авторизованы. Пожалуйста, войдите в систему.
        </div>
        <a href="/login" class="btn btn-primary">Перейти на страницу входа</a>
    </#if>

</div>
</body>
</html>
