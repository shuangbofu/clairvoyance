export function getChartOption(chart, data) {
  let option = {
    xAxis: [],
    yAxis: [],
    series: []
  }
  chart.sqlConfig.x.forEach(xl => {
    const xData = data.map(i => i[xl.realAliasName])
    option.xAxis.push({
      type: 'category',
      data: xData
    })
  })

  chart.sqlConfig.y.forEach(yl => {
    const yData = data.map(i => i[yl.realAliasName])
    option.yAxis.push({
      type: 'value'
    })
    option.series.push({
      type: map[chart.chartType],
      data: yData
    })
  })
  console.log(option)
  return option
}

const map = {
  C5: 'bar',
  C4: 'line',
  c6: 'pie'
}

export const chartTypes = [
  {
    name: "C1",
    title: "表格"
  },
  {
    name: "C2",
    title: "指标卡"
  },
  {
    name: "C3",
    title: "计量图"
  },
  {
    name: "C4",
    title: "折线图"
  },
  {
    name: "C5",
    // 簇状
    title: "柱状图"
  },
  {
    name: "C6",
    title: "饼图"
  }
];