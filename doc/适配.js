{
    let test =  {"layoutType":"maxArea","screenAlign":"mid"}
    let LayoutType = {
        // 不产生适配行为，但可识别位移操作
        None: 'none',
        //
        // 以宽度或高度缩放比例小的值进行等比例拉伸 （如果宽度和高度缩放比例大小不相等，会出现黑边）
        ShowAll: 'showAll',
        //
        // 以宽度或高度缩放比例大的值进行等比例拉伸 （如果宽度和高度缩放比例大小不相等，会出现出框 出框部分被裁剪）
        NoBorder: 'noBorder',
        //
        // 以宽度或高度各自的缩放比例进行非等比例拉伸 （如果宽度和高度缩放比例大小不相等，会出现图像变形）
        ExactFit: 'exactFit',
        //
        // 以宽度充满全屏的缩放比例进行等比例拉伸 （对于宽屏屏幕来说，纵向可能会有图像出屏）
        FullWidth: 'fullWidth',
        //
        // 以高度充满全屏的缩放比例进行等比例拉伸 （对于方屏屏幕来说，横向可能会有图像出屏）
        FullHeight: 'fullHeight',
        //
        // 非等比拉伸 以宽度充满全屏的缩放比例进行拉伸, 高度按ShowAll的方式拉伸（如果宽度和高度缩放比例大小不相等，会出现图像变形）
        OnlyFullWidth: 'onlyFullWidth',
        //
        // 非等比拉伸 以高度充满全屏的缩放比例进行拉伸, 宽度按ShowAll的方式拉伸 （如果宽度和高度缩放比例大小不相等，会出现图像变形）
        OnlyFullHeight: 'onlyFullHeight',
        //
        // 等比拉伸 以画布与节点资源图片宽高比 大的比例值为基础 宽度等比拉伸 （用于处理比画布大的图片，并让图片尽量多的显示在屏幕内）
        MaxArea: 'maxArea',
        //
        // 嵌入的子csd节点需要适配
        Embed: 'embed'
    }

    // 对齐类型
    let AlignType = {
        // 顶对齐
        Top: 'top',
        //
        // 底对齐
        Bottom: 'bottom',
        //
        // 中间对齐
        Mid: 'mid',
        //
        // 左对齐
        Left: 'left',
        //
        // 右对齐
        Right: 'right',
        //
        // x轴中间对齐
        MidW: 'midW',
        //
        // y轴中间对齐
        MidH: 'midH'
    }
}