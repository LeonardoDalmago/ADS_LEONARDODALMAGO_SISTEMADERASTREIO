/* Portal de Rastreamento Tecnotri: tema claro/escuro e menu lateral */
(function () {
    var CHAVE = 'tt-theme';

    function aplicarTema(tema) {
        document.documentElement.setAttribute('data-theme', tema);
    }

    // Roda já no <head>, antes de a página aparecer, para não piscar.
    var salvo = null;
    try { salvo = window.localStorage.getItem(CHAVE); } catch (e) { /* sem storage */ }
    aplicarTema(salvo === 'dark' ? 'dark' : 'light');

    document.addEventListener('DOMContentLoaded', function () {
        var botaoTema = document.getElementById('tt-theme-toggle');
        if (botaoTema) {
            botaoTema.addEventListener('click', function () {
                var atual = document.documentElement.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
                aplicarTema(atual);
                try { window.localStorage.setItem(CHAVE, atual); } catch (e) { /* sem storage */ }
            });
        }

        var botaoMenu = document.getElementById('tt-menu-btn');
        var mascara = document.getElementById('tt-mask');
        var corpo = document.body;

        if (botaoMenu) {
            botaoMenu.addEventListener('click', function () {
                if (window.innerWidth <= 991) {
                    corpo.classList.toggle('tt-menu-open');
                } else {
                    corpo.classList.toggle('tt-collapsed');
                }
            });
        }
        if (mascara) {
            mascara.addEventListener('click', function () {
                corpo.classList.remove('tt-menu-open');
            });
        }
    });
})();
