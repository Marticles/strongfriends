$(".btn").click(function () {
    var gender = $('#input_gender').val();
    var weight = parseFloat($('#input_weight').val());
    var squat = parseFloat($('#squat').val());
    var bench = parseFloat($('#bench').val());
    var deadlift = parseFloat($('#deadlift').val());
    var total = squat + bench + deadlift;
    if (gender =="men"){
        var wilks = total*500/(-216.0475144+(16.2606339*weight)+(-0.002388645*Math.pow(weight,2))+(-0.00113732*Math.pow(weight,3))+  (7.01863E-06*Math.pow(weight,4)) + (-1.291E-08*Math.pow(weight,5)) );
    }
    else{
        var wilks = total*500/(594.31747775582+(-27.23842536447*weight)+(0.82112226871*Math.pow(weight,2))+(-0.00930733913*Math.pow(weight,3))+  (4.731582E-05*Math.pow(weight,4)) + (-9.054E-08*Math.pow(weight,5)) );
    }
    wilks = parseFloat(wilks.toFixed(2));
    $("#wilks").html("维京系数：" + wilks);

    $.ajax({
        url: "/rank/addRank",
        type: "POST",
        async: true,
        data:{
            "gender":gender,
            "weight":weight,
            "squat":squat,
            "bench":bench,
            "deadlift":deadlift,
            "total":total,
            "wilks":wilks
        },
        dataType: "json",
        success: function(data){
            console.log(data);
        },
        error:function(err){
            console.log(err.statusText);
        }
    });




})


