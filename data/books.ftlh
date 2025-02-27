<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Список книг</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
    <h1 class="text-center text-primary mb-4">Список книг</h1>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
        <#list books as book>
            <div class="col">
                <div class="card h-100 shadow-sm text-center d-flex flex-column p-4">
                    <img src="${book.imagePath}" class="card-img-top img-fluid object-fit-cover" style="height: 500px;" alt="${book.title}">
                    <div class="card-body flex-grow-1 ">
                        <h5 class="card-title">${book.title}</h5>
                        <p class="card-text text-muted">Автор: ${book.author}</p>
                    </div>
                    <div class="mt-auto">
                        <a href="/book/${book.id}" class="btn btn-primary">Подробнее</a>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
