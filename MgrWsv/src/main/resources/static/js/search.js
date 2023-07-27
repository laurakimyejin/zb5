//
// $(document).ready(function(){
//     $("#searchBtn").on("click",function(e) {
//         e.preventDefault();
//         page(0);
//     });
// });
function searchBtn(){
    var searchBy = $("#searchBy").val();
    var searchQuery = $("#searchQuery").val();

    location.href="/voice/list" +"/"
    + "&searchBy=" + searchBy
    + "&searchQuery=" + searchQuery;
}

