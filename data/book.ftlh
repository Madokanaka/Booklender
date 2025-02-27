<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Информация о книге</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
<div class="container py-5">
    <div class="card shadow-lg p-4">
        <div class="row g-4">
            <div class="col-md-4 text-center">
                <img src="${book.imagePath}" class="img-fluid rounded img-thumbnail object-fit-cover" style="max-height: 500px; width: 100%;" alt="Обложка книги">
            </div>
            <div class="col-md-8">
                <h1 class="text-primary">${book.title}</h1>
                <p class="fs-5"><strong>Автор:</strong> ${book.author}</p>
                <p class="fs-5"><strong>Статус:</strong>
                    <span class="badge bg-${book.available?string('success','danger')}">
                            ${book.available?string('Доступна', 'Выдана')}
                        </span>
                </p>
                <p class="fs-5"><strong>Описание:</strong></p>
                <p class="text-muted">${book.description}</p>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
