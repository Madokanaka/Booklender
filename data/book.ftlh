<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Информация о книге</title>
</head>
<body>
<h1>${book.title}</h1>
<p><strong>Автор:</strong> ${book.author}</p>
<p><strong>Статус:</strong>
    <#if book.available>
        Доступна
    <#else>
        Выдана
    </#if>
</p>
<img src="${book.imagePath}" alt="Обложка книги">
</body>
</html>
