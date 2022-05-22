<template>
  <div class="charts-view-container">
    <div class="filter-wrapper">
    </div>
    <div class="content">
       
        <div style="display: flex;flex-wrap:wrap">
          <!--条形图 task count-->
          <div>
            <div id="taskCount" style="width: 400px;height: 400px"></div>
          </div>
          <!--饼图 task rate-->
          <div>
              <div id="taskRate" style="width: 400px;height: 400px"></div>
          </div>
        </div>
       
       <div id="keywords" style="display: flex;flex-wrap:wrap">
          <!--饼图 keywords rate-->
          <!-- <div style="display: flex;width: 100%">
              <div id="wordRate-${index}" style="width: 400px;height: 400px"></div>
          </div> -->
       </div>

    </div>
  </div>
</template>
  
<script>

import axios from 'axios'
 
export default {
  name: "Home",
  components: {
    
  },
  data() {
    return {
      inpContent: '',
      dialogVisible: false,
      param: {}
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.getStatisticsInfo();
    });
  },
  methods: {
    getStatisticsInfo() {
      let param = {
        taskId: this.$route.params.id,
        isSort: true,
        num: 10
      }
      console.log(param);
      this.param = param
      axios.post('http://localhost:26001/spider-nlp/statistics/get', param).then(response => {
        this.items = response.data.data
        this.showTaskCount()
        this.showTaskRate()
        this.showWordsRate()
      }).catch(function (error) { // 请求失败处理
        console.log(error);
      })
    },
    showTaskCount() {
      let echarts = require('echarts');
      let chartDom = document.getElementById('taskCount');
      let myChart = echarts.init(chartDom);
      let option;
      option = {
        title: {
          text: '任务情感分析柱状图统计'
        },
        tooltip: {},
        legend: {
          data: ['Count']
        },
        xAxis: {
          type: 'category',
          data: ['总结果' ,'消极倾向', '积极倾向', '中性']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [this.items.totalCount, this.items.negativeCount, this.items.positiveCount, this.items.neutralCount],
            type: 'bar'
          }
        ]
      };
      option && myChart.setOption(option);
    },
    showTaskRate() {
      let echarts = require('echarts');
      let chartDom = document.getElementById('taskRate');
      let myChart = echarts.init(chartDom);
      let option;
      option = {
        title: {
          text: '任务情感分析饼状状图统计'
        },
        tooltip: {},
        legend: {
          data: ['rate']
        },
        series: [
          {
            type: 'pie',
            data: [
              {
                value: (this.items.negativeCount/this.items.totalCount * 100).toString().substring(0, 4),
                name: '消极倾向占比%'
              },
              {
                value: (this.items.positiveCount/this.items.totalCount * 100).toString().substring(0, 4),
                name: '积极倾向占比%'
              },
              {
                value: (this.items.neutralCount/this.items.totalCount * 100).toString().substring(0, 4),
                name: '中性倾向占比%'
              }
            ],
            radius: '50%'
          }
        ]
      };
      option && myChart.setOption(option);
    },
    showWordsRate(){
      let num = this.param.num
      let wordsDom = document.getElementById("keywords");
      for(let i = 0; i < num; i++){
        wordsDom.innerHTML += '<div><div id="wordRate-' + i +'" style="width: 400px;height: 400px"></div></div>'
      }
      for(let i = 0; i < num; i++){
        this.confWordsRate('wordRate-' + i, this.items.keyWordsResult.keyWords[i]);
      }
    },
    confWordsRate(id, keyword) {
      let echarts = require('echarts');
      let chartDom = document.getElementById(id);
      let myChart = echarts.init(chartDom);
      let option;
      option = {
        title: {
          text: '['+ keyword.content +']情感分析饼状状图统计'
        },
        tooltip: {},
        legend: {
          data: ['rate']
        },
        series: [
          {
            type: 'pie',
            data: [
              {
                value: keyword.negativeCount,
                name: '消极倾向占比：' + (keyword.negativeCount/keyword.totalCount * 100).toString().substring(0, 4) + "%"
              },
              {
                value: keyword.positiveCount,
                name: '积极倾向占比：' + (keyword.positiveCount/keyword.totalCount * 100).toString().substring(0, 4) + "%"
              },
              {
                value: keyword.neutralCount,
                name: '中性倾向占比：' + (keyword.neutralCount/keyword.totalCount * 100).toString().substring(0, 4) + "%"
              }
            ],
            radius: '50%'
          }
        ]
      };
      option && myChart.setOption(option);
    }
  }
}
</script>
 
<style scoped>
  .filter-wrapper {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
  }

  .content {
    display: block;
  }

  .charts-view-wrapper {
    float: left;
    flex-basis: calc(100% - 240px);
    width: calc(100% - 300px);
    transition: width 0.3s;
  }

  .charts-view-wrapper.errors-collapsed {
    flex-basis: 100%;
    width: 100%;
  }

  .charts-view {
    margin-top: 0 !important;
    overflow-y: scroll !important;
    height: 600px;
    list-style: none;
    color: #A9B7C6;
    background: #2B2B2B;
    border: none;
  }

  .errors-wrapper {
    float: left;
    display: inline-block;
    margin: 0;
    padding: 0;
    flex-basis: 240px;
    width: 300px;
    transition: opacity 0.3s;
    border-top: 1px solid #DCDFE6;
    border-right: 1px solid #DCDFE6;
    border-bottom: 1px solid #DCDFE6;
    height: calc(100vh - 240px);
    font-size: 16px;
    overflow: auto;
  }

  .errors-wrapper.collapsed {
    width: 0;
  }

  .errors-wrapper .error-list {
    list-style: none;
    padding: 0;
    margin: 0;
  }

  .errors-wrapper .error-list .error-item {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    /*height: 18px;*/
    border-bottom: 1px solid white;
    padding: 5px 0;
    background: #F56C6C;
    color: white;
    cursor: pointer;
  }

  .errors-wrapper .error-list .error-item.active {
    background: #E6A23C;
    font-weight: bolder;
    text-decoration: underline;
  }

  .errors-wrapper .error-list .error-item:hover {
    font-weight: bolder;
    text-decoration: underline;
  }

  .errors-wrapper .error-list .error-item .line-no {
    display: inline-block;
    text-align: right;
    width: 70px;
  }

  .errors-wrapper .error-list .error-item .line-content {
    display: inline;
    width: calc(100% - 70px);
    padding-left: 10px;
  }

  .right {
    display: flex;
    align-items: center;
  }

  .right .el-pagination {
    margin-right: 10px;
  }
</style>
