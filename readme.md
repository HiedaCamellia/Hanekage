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
      "texture": "your:path/to/texture.png"
    }
  ],
  "default_trail_time":20,
  "default_trail_color":16777215
}
```
`trail_time`是记录刀刃的总帧数（目前是这样）  
`color`是刀光的颜色，这里的`16711680`转成十六进制就是`#FF0000`，也就是红色。  
`texture`是刀光的纹理路径，是可选项
