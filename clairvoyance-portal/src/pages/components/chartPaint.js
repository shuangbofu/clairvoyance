export function getChartOption(chartLayer, data) {
  const chartType = map[chartLayer.chartType]
  const xs = chartLayer.x.map(i => i.realAliasName)
  const ys = chartLayer.y.map(i => i.realAliasName)
  console.log(chartType)
  console.log(chartLayer)

  // 折线、柱状
  if (['line', 'bar', 'bar2'].includes(chartType)) {

    let xAxis = xs.map(i => {
      return {
        type: 'category',
        data: data.map(values => values[i])
      }
    })
    if (xs.length === 0) {
      xAxis = [{ type: 'category', data: [] }]
    }

    const yAxis = ys.map(() => {
      return { type: 'value' }
    })
    const series = ys.map(i => {
      return {
        name: i,
        type: chartType,
        label: {
          show: true
        },
        data: data.map(values => values[i]),
      }
    })
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} : {c}'
      },
      legend: {
        data: ys
      },
      xAxis,
      yAxis,
      series
    }
    // 条形图
    if (chartLayer.chartType === 'C7') {
      option.xAxis = yAxis
      option.yAxis = xAxis
    }
    return option
    // 饼图
  } else if (chartType === 'pie') {
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b} : {c} ({d}%)'
      },
      legend: {
        top: 0,
        right: 0,
        data: [],
        orient: 'vertical',
        icon: 'circle',
        textStyle: {
          fontSize: 11
        }
      },
      series: {
        type: 'pie',
        data: [],
        center: ['40%', '55%'],
      }
    }
    // 一维度一值
    if (xs.length === 1 && ys.length === 1) {
      option.legend.data = data.map(i => i[xs[0]])
      option.series.data = data.map(i => {
        return {
          name: i[xs[0]],
          value: i[ys[0]]
        }
      })
      // 零维度多值
    } else if (xs.length == 0 && ys.length > 1) {
      option.legend.data = ys
      option.series.data = ys.map(i => {
        return {
          name: i,
          value: data.map(j => j[i])[0]
        }
      })
    } else {
      throw new Error('错误')
    }
    return option
    // 计量图
  } else if (chartType === 'gauge') {
    return {
      series: [{
        type: 'gauge',
        data: [{
          name: ys[0],
          value: data.map(i => i[ys[0]])[0]
        }]
      }]
    }
  }
}

const map = {
  C3: 'gauge',
  C5: 'bar',
  C4: 'line',
  C6: 'pie',
  C7: 'bar'
}

export const chartTypes = [
  // {
  //   name: "C1",
  //   title: "表格"
  // },
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
  },
  {
    name: 'C7',
    title: '条形图'
  }
];