<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список книг</title>
    <link rel="stylesheet" href="/css/styleOfPages.css">
</head>
<body>
<div class="container">
    <h1>Список книг</h1>
    <ul class="book-list">
        <#list books as book>
            <li class="book-item">
                <div class="book-card">
                    <img src="${book.imagePath}" alt="${book.title}">
                    <h4>${book.title}</h4>
                    <p>Автор: ${book.author}</p>
                    <a href="/book/${book.id}">Подробнее</a>
                </div>
            </li>
        </#list>
    </ul>
</div>
</body>
</html>
