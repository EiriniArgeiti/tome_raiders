$(function () {

    $("a.confirmDeletion").click(function () {
        if (!confirm("confirmDeletion")) return false;
    })

});