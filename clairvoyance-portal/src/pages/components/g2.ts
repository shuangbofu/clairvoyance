import { Chart, Geometry } from '@antv/g2';
import { AttributeOption } from '@antv/g2/lib/interface'
import DataSet from '@antv/data-set';
import { TransformsParams } from '@antv/data-set/lib/transform-params'
import { View } from '@antv/data-set/lib/view'
declare type TransformOptions<T extends keyof TransformsParams = any> = {
  type: T;
} & TransformsParams[T];
enum ChartType {
  C1 = "表格",
  C4 = "折线图",
  C5 = "柱状图"
}

class CChart {
  container: HTMLElement
  chart: Chart
  option: Option
  ds: DataSet

  constructor(container: HTMLElement, option: Option) {
    this.container = container
    this.option = option
    this.chart = new Chart({
      container,
      autoFit: true,
      theme: option?.theme ?? 'default',
      padding: [50, 40, 60, 20],
    });
    this.ds = new DataSet()
  }
  resize() {
    const width = this.container.offsetWidth
    const height = this.container.offsetHeight
    this.chart.changeSize(width, height);
  }
  render(data: any) {
    const view = this.createView(data)
    this.getGeometry(view);
    this.chart.changeData(data)
  }
  createView(data: any) {
    return this.ds.createView()
      .source(data)
      .transform(this.option.transformOption);
  }
  getGeometry(view: View) {
    // let geometries: Geometry[] = []
    let geometry: Geometry
    const chartType = this.option?.chartType
    if (!chartType) {
      return
    }
    switch (chartType) {
      case "C4":
        geometry = this.chart.line()
        break;
      case "C5":
      default:
        geometry = this.chart.interval()
        break;
    }
    geometry.position(view.rows)
    // this.chart.interval().position('type*value');
  }
}
Geometry

class Option {
  theme: String
  chartType: String
  position: AttributeOption
  transformOption: TransformOptions
  xAis: String[]

  constructor(theme: String, chartType: ChartType) {
    this.theme = theme || "default"
    // this.chartType = (<any>ChartType)[chartType as any] || ChartType.C1.toString
    this.chartType = chartType
  }
  setXY<T, R>(xAis: Array<String>, yAis: Array<R>): Option {
    this.xAis = xAis
    this.transformOption = {
      type: 'fold',
      fields: yAis,
      key: 'type',
      value: 'value',
    }
    return this
  }
}

// function getChart(container: HTMLElement) {
//   const chart = new Chart({
//     container,
//     autoFit: true,
//     padding: [50, 40, 60, 20],
//   });
// chart.scale('value', {
//   alias: '销售额(万)',
// });

// chart.axis('type', {
//   tickLine: {
//     alignTick: false,
//   },
// });
// chart.axis('value', false);

// chart.tooltip({
//   showMarkers: false,
// });

// chart.interaction('element-active');

// 添加文本标注
// data.forEach((item) => {
//   chart
//     .annotation()
//     .text({
//       position: [item.type, item.value],
//       content: item.value,
//       style: {
//         textAlign: 'center',
//       },
//       offsetY: -30,
//     })
//     .text({
//       position: [item.type, item.value],
//       content: (item.percent * 100).toFixed(0) + '%',
//       style: {
//         textAlign: 'center',
//       },
//       offsetY: -12,
//     });
// });
//   return chart
// }

export {
  CChart as Chart,
  Option
}