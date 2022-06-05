<template>
  <div class="charts-view-container">
    <div class="filter-wrapper">
    </div>
    <div class="content" v-if="items">
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
        <div v-for="(word, index) in (items.keyWordsResult && items.keyWordsResult.keyWords)"
          style="display: flex;margin-bottom: 50px;">
          <el-card class="box-card" style="margin: 0 24px 0 24px;">
            <div ref="wordRate" style="width: 400px;height: 400px;"></div>
            <div style="width: 400px;margin-top: -35px;">
              <el-table :data="word.sources" size="medium" style="width: 100%;height: 300px;overflow: scroll" border>
                <el-table-column type="index" width="50">
                </el-table-column>
                <el-table-column prop="data" label="主题来源" width="180">
                </el-table-column>
                <!-- <el-table-column prop="url" label="URL" width="180">
                </el-table-column> -->
                <el-table-column label="URL">
                  <template slot-scope="scope">
                    <a class="href" :href="scope.row.url" target="_blank">{{ scope.row.url }}</a>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-card>
        </div>
      </div>

    </div>
    <div v-else class="l-center">

      <div class="l-img">
        <svg viewBox="0 0 79 86" version="1.1" xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink">
          <defs>
            <linearGradient id="linearGradient-1-4" x1="38.8503086%" y1="0%" x2="61.1496914%" y2="100%">
              <stop stop-color="#FCFCFD" offset="0%"></stop>
              <stop stop-color="#EEEFF3" offset="100%"></stop>
            </linearGradient>
            <linearGradient id="linearGradient-2-4" x1="0%" y1="9.5%" x2="100%" y2="90.5%">
              <stop stop-color="#FCFCFD" offset="0%"></stop>
              <stop stop-color="#E9EBEF" offset="100%"></stop>
            </linearGradient>
            <rect id="path-3-4" x="0" y="0" width="17" height="36"></rect>
          </defs>
          <g id="Illustrations" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
            <g id="B-type" transform="translate(-1268.000000, -535.000000)">
              <g id="Group-2" transform="translate(1268.000000, 535.000000)">
                <path id="Oval-Copy-2"
                  d="M39.5,86 C61.3152476,86 79,83.9106622 79,81.3333333 C79,78.7560045 57.3152476,78 35.5,78 C13.6847524,78 0,78.7560045 0,81.3333333 C0,83.9106622 17.6847524,86 39.5,86 Z"
                  fill="#F7F8FC"></path>
                <polygon id="Rectangle-Copy-14" fill="#E5E7E9"
                  transform="translate(27.500000, 51.500000) scale(1, -1) translate(-27.500000, -51.500000) "
                  points="13 58 53 58 42 45 2 45"></polygon>
                <g id="Group-Copy"
                  transform="translate(34.500000, 31.500000) scale(-1, 1) rotate(-25.000000) translate(-34.500000, -31.500000) translate(7.000000, 10.000000)">
                  <polygon id="Rectangle-Copy-10" fill="#E5E7E9"
                    transform="translate(11.500000, 5.000000) scale(1, -1) translate(-11.500000, -5.000000) "
                    points="2.84078316e-14 3 18 3 23 7 5 7"></polygon>
                  <polygon id="Rectangle-Copy-11" fill="#EDEEF2"
                    points="-3.69149156e-15 7 38 7 38 43 -3.69149156e-15 43"></polygon>
                  <rect id="Rectangle-Copy-12" fill="url(#linearGradient-1-4)"
                    transform="translate(46.500000, 25.000000) scale(-1, 1) translate(-46.500000, -25.000000) " x="38"
                    y="7" width="17" height="36"></rect>
                  <polygon id="Rectangle-Copy-13" fill="#F8F9FB"
                    transform="translate(39.500000, 3.500000) scale(-1, 1) translate(-39.500000, -3.500000) "
                    points="24 7 41 7 55 -3.63806207e-12 38 -3.63806207e-12"></polygon>
                </g>
                <rect id="Rectangle-Copy-15" fill="url(#linearGradient-2-4)" x="13" y="45" width="40" height="36">
                </rect>
                <g id="Rectangle-Copy-17" transform="translate(53.000000, 45.000000)">
                  <mask id="mask-4-4" fill="white">
                    <use xlink:href="#path-3-4"></use>
                  </mask>
                  <use id="Mask" fill="#E0E3E9"
                    transform="translate(8.500000, 18.000000) scale(-1, 1) translate(-8.500000, -18.000000) "
                    xlink:href="#path-3-4"></use>
                  <polygon id="Rectangle-Copy" fill="#D5D7DE" mask="url(#mask-4-4)"
                    transform="translate(12.000000, 9.000000) scale(-1, 1) translate(-12.000000, -9.000000) "
                    points="7 0 24 0 20 18 -1.70530257e-13 16"></polygon>
                </g>
                <polygon id="Rectangle-Copy-18" fill="#F8F9FB"
                  transform="translate(66.000000, 51.500000) scale(-1, 1) translate(-66.000000, -51.500000) "
                  points="62 45 79 45 70 58 53 58"></polygon>
              </g>
            </g>
          </g>
        </svg>
      </div>
      <div class="l-text"> 数据正在处理中....</div>
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
      items: null
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
        console.log('----items', this.items);
        this.showTaskCount()
        this.showTaskRate()
        this.showWordsRate()
      }).catch(function (error) { // 请求失败处理
        console.log(error);
      })
    },
    showTaskCount() {
      this.$nextTick(function () {
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
            data: ['总结果', '消极倾向', '积极倾向', '中性']
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
      this.$nextTick(function () {
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
                  value: (this.items.negativeCount / this.items.totalCount * 100).toString().substring(0, 4),
                  name: '消极倾向占比%'
                },
                {
                  value: (this.items.positiveCount / this.items.totalCount * 100).toString().substring(0, 4),
                  name: '积极倾向占比%'
                },
                {
                  value: (this.items.neutralCount / this.items.totalCount * 100).toString().substring(0, 4),
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
    showWordsRate() {
      let num = this.param.num
      if (this.items.keyWordsResult == null || this.items.keyWordsResult.keyWords == null) {
        return
      }
      for (let i = 0; i < num; i++) {
        this.confWordsRate('wordRate', i, this.items.keyWordsResult.keyWords[i]);
      }
    },
    confWordsRate(id, index, keyword) {
      this.$nextTick(function () {
        let echarts = require('echarts');
        let chartDom = this.$refs[id][index];
        let myChart = echarts.init(chartDom);
        let option;
        option = {
          title: {
            text: '[' + keyword.content + ']情感分析饼状图统计'
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
                  name: '消极倾向占比：' + (keyword.negativeCount / keyword.totalCount * 100).toString().substring(0, 4) + "%"
                },
                {
                  value: keyword.positiveCount,
                  name: '积极倾向占比：' + (keyword.positiveCount / keyword.totalCount * 100).toString().substring(0, 4) + "%"
                },
                {
                  value: keyword.neutralCount,
                  name: '中性倾向占比：' + (keyword.neutralCount / keyword.totalCount * 100).toString().substring(0, 4) + "%"
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
.l-center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 690px;
}

.l-img {
  width: 160px;
  height: 174px;
}

.l-text {
  margin-top: 20px;
  font-size: 14px;
  color: #5e6d82;
  line-height: 1.5em;
}

.href {
  color: #409eff;
  text-decoration: underline;
}

.href:hover {
  cursor: pointer;
}

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
