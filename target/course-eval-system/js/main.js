// Auto-dismiss alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function () {
    var alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(function () { alert.remove(); }, 500);
        }, 5000);
    });

    // Animate result bars on load
    var bars = document.querySelectorAll('.result-bar-fill');
    bars.forEach(function (bar) {
        var targetWidth = bar.style.width;
        bar.style.width = '0%';
        setTimeout(function () { bar.style.width = targetWidth; }, 100);
    });

    // Confirm before form deletes (backup for inline onsubmit)
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!confirm(form.dataset.confirm)) e.preventDefault();
        });
    });
});
