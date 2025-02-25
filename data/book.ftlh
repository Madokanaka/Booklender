<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Информация о книге</title>
    <link rel="stylesheet" href="/css/styleOfPages.css">
</head>

<body>
<div class="container">
    <div class="book-details">
        <h1 class="book-title">${book.title}</h1>
        <p class="book-author"><strong>Автор:</strong> ${book.author}</p>
        <p class="book-status"><strong>Статус:</strong>
            <#if book.available>
                <span class="available">Доступна</span>
            <#else>
                <span class="unavailable">Выдана</span>
            </#if>
        </p>
        <div class="book-info">
            <div class="book-image">
                <img src="${book.imagePath}" class="description-photo" alt="Обложка книги">
            </div>
            <p class="book-description"><strong>Описание:</strong> ${book.description}</p>
        </div>
    </div>
</div>
</body>

</html>
