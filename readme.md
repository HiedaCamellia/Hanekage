# Hanekage 羽影

这是一个用来渲染刀光的模组，配置简单，性能良好。

## 怎么让Hanekage追踪刀刃

在BlockBench中将你的刀刃的骨骼添加`-track`的后缀, eg.`bone-track`  
在该骨骼下新建两个骨骼，后缀是`-trackstart`和`-trackend`, eg.`bone-trackstart`和`bone-trackend`  
这三个骨骼应该呈现如下的结构:

- bone-track
   - bone-trackstart
   - bone-trackend

## 怎么配置刀光追踪时长和颜色

在 `hanekage/config.json` 中

```json
{
  "sword_trail":[
    {
      "bone_name": "bone-track",
      "trail_time": 25,
      "color": 16711680,
      "texture": "your:path/to/texture.png",
      "interpolation": {
        "type": "catmullrom",
        "steps": 3
      },
      "auto_push": true
    }
  ],
  "default_trail_time":20,
  "default_trail_color":16777215,
  "default_trail_interpolation_type": "lerp",
  "default_trail_interpolation_steps": 3
}
```
- `bone_name`是刀刃的骨骼名称，必须与BlockBench中添加的骨骼名称一致。
- `trail_time`是记录刀刃的总帧数（目前是这样）  
- `color`是刀光的颜色，这里的`16711680`转成十六进制就是`#FF0000`，也就是红色。   
- `texture`是刀光的纹理路径，是可选项  
- `interpolation`
  - `type`是插值方式，目前支持`lerp`和`catmullrom`，分别是线性插值和Catmull-Rom插值。  
  - `steps`是插值的细分程度，数值越大越平滑，但也更耗性能。  
- `auto_push`是一个布尔值，表示是否由hanekage管理刀光，改成false后需要手动调用`HanekageAPI#pushPoint`来添加刀光。
