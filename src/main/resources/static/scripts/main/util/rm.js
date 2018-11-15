$(".btn").click(function () {
    var weight = $('#input_weight').val();
    var repetitions = $('#input_reps').val();
    var brzycki = Math.round(weight * (36 / (37 - repetitions)));
    var epley = Math.round((1 + (0.0333 * repetitions)) * weight);
    var lander = Math.round((100 * weight) / (101.3 - 2.67123 * repetitions));
    var lombardi = Math.round(weight * (Math.pow(repetitions, 0.1)));
    var mayhewetal = Math.round((100 * weight) / (52.2 + (41.9 * Math.exp(-0.055 * repetitions))));
    var oconneretal = Math.round(weight * (1 + 0.025 * repetitions));
    var wathan = Math.round((100 * weight) / (48.8 + (53.8 * Math.exp(-0.075 * repetitions))));
    $("#Brzycki").html("Brzycki公式：" + brzycki + "KG");
    $("#Epley").html("Epley公式：" + epley + "KG");
    $("#Lander").html("Lander公式：" + lander + "KG");
    $("#Lombardi").html("Lombardi公式：" + lombardi + "KG");
    $("#Mayhewetal").html("Mayhewetal公式：" + mayhewetal + "KG");
    $("#Oconneretal").html("Oconneretal公式：" + oconneretal + "KG");
    $("#Wathan").html("Wathan公式：" + wathan + "KG");

})

