<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Информация о сотруднике</title>
    <link rel="stylesheet" href="/employee.css">
</head>
<body>
<header>
    <h1>Информация о сотруднике</h1>
</header>

<main>
    <section class="employee-info">
        <h2>${employee.name}</h2>
        <p><strong>Должность:</strong> ${employee.position}</p>
    </section>

    <section class="books-section">
        <h3>📖 Книги, которые сейчас читает</h3>
        <#if currentBooks?size == 0>
            <p>Нет текущих книг.</p>
        <#else>
            <ul class="book-list">
                <#list currentBooks as book>
                    <li class="book-item">
                        <img src="/${book.imagePath}" alt="${book.title}">
                        <div class="book-info">
                            <h4>${book.title}</h4>
                            <p>Автор: ${book.author}</p>
                            <a href="/book/${book.id}">Подробнее</a>
                        </div>
                    </li>
                </#list>
            </ul>
        </#if>
    </section>

    <section class="books-section">
        <h3>📚 Книги, которые уже прочитал</h3>
        <#if pastBooks?size == 0>
            <p>Нет прочитанных книг.</p>
        <#else>
            <ul class="book-list">
                <#list pastBooks as book>
                    <li class="book-item">
                        <img src="/${book.imagePath}" alt="${book.title}">
                        <div class="book-info">
                            <h4>${book.title}</h4>
                            <p>Автор: ${book.author}</p>
                            <a href="/book/${book.id}">Подробнее</a>
                        </div>
                    </li>
                </#list>
            </ul>
        </#if>
    </section>
</main>
</body>
</html>
