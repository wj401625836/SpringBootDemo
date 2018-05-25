/**
 * Created by yingjing.liu on 2018/5/15.
 */

var closeWindow = function(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index);
}