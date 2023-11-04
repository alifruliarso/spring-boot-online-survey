// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('#dataTableSurvey').DataTable({
        ordering: false
    });
    $('#dataTableResponse').DataTable({
        ordering: false
    });
    $('#dataTableAnswer').DataTable({
        /* No ordering applied by DataTables during initialisation */
        "order": [],
        ordering: false
    });
});