<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Информация о сотруднике</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-5">
    <header class="text-center mb-5">
        <h1>Информация о сотруднике</h1>
    </header>

    <main>
        <section class="employee-info mb-4">
            <h2 class="mb-3">${employee.name}</h2>
            <p><strong>Должность:</strong> ${employee.position}</p>
            <p><strong>Email:</strong> ${employee.email}</p>
        </section>

        <section class="books-section mb-4">
            <h3>📖 Книги, которые сейчас читает</h3>
            <#if currentBooks?size == 0>
                <p>Нет текущих книг.</p>
            <#else>
                <ul class="list-unstyled row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                    <#list currentBooks as book>
                        <li class="col">
                            <div class="card h-100 shadow-sm text-center d-flex flex-column p-4">
                                <img src="${book.imagePath}" class="card-img-top img-fluid object-fit-cover" style="height: 500px;" alt="${book.title}">
                                <div class="card-body flex-grow-1">
                                    <h5 class="card-title">${book.title}</h5>
                                    <p class="card-text text-muted">Автор: ${book.author}</p>
                                </div>
                                <div class="mt-auto">
                                    <a href="/book/${book.id}" class="btn btn-primary">Подробнее</a>
                                </div>
                            </div>
                        </li>
                    </#list>
                </ul>
            </#if>
        </section>

        <section class="books-section mb-4">
            <h3>📚 Книги, которые уже прочитал</h3>
            <#if pastBooks?size == 0>
                <p>Нет прочитанных книг.</p>
            <#else>
                <ul class="list-unstyled row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                    <#list pastBooks as book>
                        <li class="col">
                            <div class="card h-100 shadow-sm text-center d-flex flex-column p-4">
                                <img src="${book.imagePath}" class="card-img-top img-fluid object-fit-cover" style="height: 500px;" alt="${book.title}">
                                <div class="card-body flex-grow-1">
                                    <h5 class="card-title">${book.title}</h5>
                                    <p class="card-text text-muted">Автор: ${book.author}</p>
                                </div>
                                <div class="mt-auto">
                                    <a href="/book/${book.id}" class="btn btn-primary">Подробнее</a>
                                </div>
                            </div>
                        </li>
                    </#list>
                </ul>
            </#if>
        </section>

        <section class="book-actions text-center mb-4">
            <h3>📥 Действия с книгами</h3>
            <a href="/takeBooks" class="btn btn-success btn-lg m-2">Взять книгу</a>
            <a href="/returnBook" class="btn btn-info btn-lg m-2">Вернуть книгу</a>
        </section>

    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
