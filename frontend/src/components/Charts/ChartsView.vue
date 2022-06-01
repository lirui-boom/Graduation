<template>
  <div class="charts-view-container">
    <div class="filter-wrapper">
    </div>
    <div class="content">
      <div style="padding: 24px 0;">
        <span style="font-size: 20px; font-weight: 700;">任务统计详情</span>
      </div>
      <div style="display: flex;flex-wrap:wrap">
        <!--条形图 task count-->
        <div>
          <el-card class="box-card" style="margin: 0 24px 0 24px;">
            <div ref="taskCount" style="width: 400px;height: 400px"></div>
          </el-card>
        </div>
        <!--饼图 task rate-->
        <div>
            <el-card class="box-card" style="margin: 0 24px 0 24px;">
            <div ref="taskRate" style="width: 400px;height: 400px"></div>
          </el-card>
        </div>
      </div>  
      
      <div style="padding: 24px 0; margin-top: 35px;">
        <span style="font-size: 20px; font-weight: 700;">主题统计详情</span>
      </div>
      <!--饼图 keywords rate-->
      <div style="display:flex;flex-wrap: wrap;">
        <div v-for="(word, index) in (items.keyWordsResult && items.keyWordsResult.keyWords)" style="display: flex;margin-bottom: 50px;">
          <el-card class="box-card" style="margin: 0 24px 0 24px;">
            <div ref="wordRate" style="width: 400px;height: 400px;"></div>
            <div style="width: 400px;margin-top: -35px;">
              <el-table
                :data="word.sources"
                size="medium"
                style="width: 100%;height: 300px;overflow: scroll">
                <el-table-column
                  type="index"
                  width="50">
                </el-table-column>
                <el-table-column
                  prop="data"
                  label="主题来源"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="url"
                  label="URL"
                  width="180">
                </el-table-column>
              </el-table>
            </div>
          </el-card>
      </div>
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
      param: {},
      items: {}
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
      this.$nextTick(function() {
        let echarts = require('echarts');
        let chartDom = this.$refs['taskCount'];
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
      });
    },
    showTaskRate() {
      this.$nextTick(function() {
        let echarts = require('echarts');
        let chartDom = this.$refs['taskRate'];
        let myChart = echarts.init(chartDom);
        let option;
        option = {
          title: {
            text: '任务情感分析饼状图统计'
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
      });
    },
    showWordsRate(){
      let num = this.param.num
      if(this.items.keyWordsResult == null || this.items.keyWordsResult.keyWords == null) {
        return
      }
      for(let i = 0; i < num; i++){
        this.confWordsRate('wordRate', i, this.items.keyWordsResult.keyWords[i]);
      }
    },
    confWordsRate(id, index, keyword) {
      this.$nextTick(function() {
        let echarts = require('echarts');
        let chartDom = this.$refs[id][index];
        let myChart = echarts.init(chartDom);
        let option;
        option = {
          title: {
            text: '['+ keyword.content +']情感分析饼状图统计'
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
      });
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
