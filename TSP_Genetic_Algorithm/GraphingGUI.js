/* jshint esversion: 6 */



var content = new Vue
(
    {
        el: '#content',
        data:
        {
            show_dashboard: true,
            x_style_area: "width: calc(100% - 400px);",

            instance_select: "",
            algorithm_select: ["basic","advanced"],

            instance: null,
            plain_map: null,

            trial_all: 1,
            generation_all: 100,
            individual_all: 2,
            generation_size: 1200,

            trial_n: 1,
            generation_n: 1,
            individual_n: 1,
        },
        methods:
        {
            toggleDashboard: function()
            {
                if(this.show_dashboard==true)
                {   this.x_style_area = "width: initial;";
                    this.show_dashboard = false;
                }
                else
                {   this.x_style_area = "width: calc(100% - 400px);";
                    this.show_dashboard = true;
                }
            },

            // 重绘experiment ----------------------------------------------------------------------
            drawExperiment: function()
            {
                // x 都是测试序数 //
                let xs = [];

                for(let i=0; i<this.instance["description"]["evolution_all"]; i++)
                {   xs.push(i+1);   }

                // trials的time-costs统计 - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                let timecost_ys = [];

                for(let i=1; i<this.instance["evolutions"].length; i++)
                {
                    timecost_ys.push(   this.instance["evolutions"][i][0]["time_cost"]   );
                }

                var experiment_plotly1 =
                    [
                        {
                            x: xs,
                            y: timecost_ys,
                            mode: 'lines+markers',
                            type: 'bar',
                            name: 'time costs',
                        }
                    ];

                var layout1 =
                    {
                        xaxis: {   title: 'trial'   },
                        yaxis: {   title: 'time cost'   },
                    };

                Plotly.newPlot(   $(".figure.timecost.statistics")[0], experiment_plotly1, layout1, {responsive: true}   );

                // trials的distance统计 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                let best_ys = [];
                let worst_ys = [];
                let avg_ys = [];

                for(let i=0; i<this.instance["evolutions"].length; i++)
                {
                    best_ys.push(   this.instance["evolutions"][i][this.generation_all]["best-individual-distance"]   );
                    worst_ys.push(   this.instance["evolutions"][i][this.generation_all]["worst-individual-distance"]   );
                    avg_ys.push(   this.instance["evolutions"][i][this.generation_all]["average-distance"]   );
                }

                var experiment_plotly2 =
                    [
                        {
                            x: xs,
                            y: best_ys,
                            mode: 'lines+markers',
                            fill: 'tonexty',
                            //type: 'bar',
                            name: 'best',
                        },
                        {
                            x: xs,
                            y: worst_ys,
                            mode: 'lines+markers',
                            fill: 'tonexty',
                            //type: 'bar',
                            name: 'worst',
                        },
                        {
                            x: xs,
                            y: avg_ys,
                            mode: 'lines+markers',
                            fill: 'tonexty',
                            //type: 'bar',
                            name: 'average',
                        }
                    ];


                Plotly.newPlot(   $(".figure.distance.statistics")[0], experiment_plotly2, {}, {responsive: true}   );
            },

            // 重绘evolution -----------------------------------------------------------------------
            drawEvolution: function()
            {
                // x 都是世代序数 //
                let xs = [];

                for(let i=0; i<this.generation_all; i++)
                {   xs.push(i+1);   }

                // 选中的测试的各世代max、min、avg分布 - - - - - - - - - - - - - - - - - - - - - - - -
                let best_ys = [];
                let worst_ys = [];
                let avg_ys = [];

                for(let i=1; i<=this.generation_all; i++)
                {
                    best_ys.push(   this.instance["evolutions"][this.trial_n-1][i]["best-individual-distance"]   );
                    worst_ys.push(   this.instance["evolutions"][this.trial_n-1][i]["worst-individual-distance"]   );
                    avg_ys.push(   this.instance["evolutions"][this.trial_n-1][i]["average-distance"]   );
                }

                var experiment_plotly2 =
                    [
                        {
                            x: xs,
                            y: best_ys,
                            mode: 'lines',
                            fill: 'tonexty',
                            //type: 'bar',
                            name: 'best',
                        },
                        {
                            x: xs,
                            y: worst_ys,
                            mode: 'lines',
                            fill: 'tonexty',
                            //type: 'bar',
                            name: 'worst',
                        },
                        {
                            x: xs,
                            y: avg_ys,
                            mode: 'lines',
                            fill: 'tonexty',
                            //type: 'bar',
                            name: 'average',
                        }
                    ];

                Plotly.newPlot(   $(".figure.distance.trend")[0], experiment_plotly2, {}, {responsive: true}   );
            },

            // 重绘generation ----------------------------------------------------------------------
            drawGeneration: function()
            {
                // x 都是世代序数 //
                let xs = [];

                for(let i=0; i<this.generation_size; i++)
                {   xs.push(i+1);   }

                // 选中的世代的所有个体的distance分布 - - - - - - - - - - - - - - - - - - - - - - - -
                let ys = [];

                for(let i=0; i<this.generation_size; i++)
                {  ys.push(   this.instance["evolutions"][this.trial_n-1][this.generation_n]["individual-distances"][i]   );   }

                let experiment_plotly2 =
                    [
                        {
                            x: xs,
                            y: ys,
                            mode: 'lines',
                            fill: 'tonexty',
                            name: 'individual distance',
                        }
                    ];

                //console.log(experiment_plotly2);
                Plotly.newPlot(   $(".figure.distance.distribution")[0], experiment_plotly2, {}, {responsive: true}   );

                // 显示数据 //
                $("#gen_fit_best").text( this.instance["evolutions"][this.trial_n-1][this.generation_n]["best-individual-distance"] );
                $("#gen_fit_avg").text( this.instance["evolutions"][this.trial_n-1][this.generation_n]["average-distance"] );
                $("#gen_fit_worst").text( this.instance["evolutions"][this.trial_n-1][this.generation_n]["worst-individual-distance"] );
            },

            // 重绘individual ----------------------------------------------------------------------
            drawIndividual: function()
            {
                let ids = [];
                let xs = [];
                let ys = [];

                console.log(this.plain_map);

                if( this.individual_n == 1 )    //best
                {
                    ids = this.instance["evolutions"][this.trial_n-1][this.generation_n]["best-individual-sequence"];
                    console.log(ids);
                    ids.push(ids[0]);
                    for(let i=0; i<this.instance["description"]["city_all"]+1; i++)
                    {
                        xs.push( this.plain_map[1][ ids[i]-1 ] );
                        ys.push( this.plain_map[2][ ids[i]-1 ] );
                    }
                }
                else if( this.individual_n == 2 )    //worst
                {
                    ids = this.instance["evolutions"][this.trial_n-1][this.generation_n]["worst-individual-sequence"];
                    ids.push(ids[0]);
                    for(let i=0; i<this.instance["description"]["city_all"]+1; i++)
                    {
                        xs.push( this.plain_map[1][ ids[i]-1 ] );
                        ys.push( this.plain_map[2][ ids[i]-1 ] );
                    }
                }

                let individual_plotly =
                    [
                        {
                            x: xs,
                            y: ys,
                            text: ids,
                            mode: 'lines+markers',
                            name: 'individual route',
                        }
                    ];

                console.log(individual_plotly);
                Plotly.newPlot(   $(".figure.route")[0], individual_plotly, {}, {responsive: true}   );

                // 在文本域输出序列 //
                $(".dashboard textarea").text( ids.toString() );
            },

            // 重绘plain map -----------------------------------------------------------------------
            drawPlainMap: function()
            {
                this.plain_map = null;

                if(this.instance_select == "WesternSahara")
                {
                    this.plain_map = plain_map_WesternSahara;
                    this.instance = advanced_WesternSahara;
                    this.trial_all = this.instance["description"]["evolution_all"];
                    this.generation_all = this.instance["description"]["generation_all"];
                    this.generation_size =  this.instance["description"]["generation_size"];
                }
                else if(this.instance_select == "Uruguay")
                {
                    this.plain_map = plain_map_Uruguay;
                    this.instance = advanced_Uruguay;
                    this.trial_all = this.instance["description"]["evolution_all"];
                    this.generation_all = this.instance["description"]["generation_all"];
                    this.generation_size =  this.instance["description"]["generation_size"];
                }
                else if(this.instance_select == "Canada")
                {
                    this.plain_map = plain_map_Canada;
                    this.instance = advanced_Canada;
                    this.trial_all = this.instance["description"]["evolution_all"];
                    this.generation_all = this.instance["description"]["generation_all"];
                    this.generation_size =  this.instance["description"]["generation_size"];
                }

                var plain_map_plotly =
                    {
                        text: this.plain_map[0],
                        x: this.plain_map[1],
                        y: this.plain_map[2],
                        mode: 'markers',
                        type: 'scatter',
                    };

                Plotly.newPlot(   $(".figure.map")[0], [plain_map_plotly], {}, {responsive: true}   );

            },
        },
    }
);


// parse source data to plain map /////////////////////////////////////////////////////////////////
function parseCoords(map)
{
    var plotly_coords = [ [],[],[] ];
    for(let city of map)
    {
        plotly_coords[0].push(city[0]);    //id
        plotly_coords[1].push(city[1]);    //x-axis
        plotly_coords[2].push(city[2]);    //y-axis
    }
    return plotly_coords;
}

var plain_map_WesternSahara = parseCoords(WesternSahara);
var plain_map_Uruguay = parseCoords(Uruguay);
var plain_map_Canada = parseCoords(Canada);




//
