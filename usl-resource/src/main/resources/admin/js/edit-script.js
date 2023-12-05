layui.use(['form'], function () {
    const form = layui.form, layer = layui.layer

    const editor = CodeMirror.fromTextArea(document.getElementById("content"), {  // 标识到textarea
        // 是否智能缩进
        smartIndent: true,
        // Tab缩进，默认4
        tabSize: 4,
        // 是否只读，默认false
        readOnly: false,
        // 在选择时是否显示光标
        showCursorWhenSelecting: true,
        // 显示行数
        lineNumbers: true,
        // 缩进单位为4
        indentUnit: 4,
        // 当前行背景高亮
        styleActiveLine: true,
        // 括号匹配
        matchBrackets: true,
        // 设置编辑器语言为JavaScript
        mode: "javascript",
        // 自动换行
        lineWrapping: true,
        // 主题
        theme: 'darcula',
        // 自动聚焦
        autofocus: true
    });

    //设置代码框的长宽
    editor.setSize('100%', '650px');

    const url = layui.url();
    let search = url.search;
    console.log(search)
    $.post('/usl/admin/api/script?scriptName=' + search.scriptName + '&belongs=' + search.belongs + '&edit=true', function (result) {
        if (result.code === 'success') {
            $('#scriptName').val(result.data.scriptName);
            $('#belongs').val(result.data.belongs);
            editor.setValue(result.data.content);
        }
        if (result.code === 'failure') {
            layer.msg(result.message, {
                icon: 2,
                time: 2000,
                anim: 5
            }, function () {
                if (result.data != null) {
                    window.location.href = result.data;
                }
            })
        }
    });

    // 监听提交
    form.on('submit(saveBtn)', function (data) {
        data.field.content = editor.getValue();

        $.ajax({
            type: 'post',
            url: '/usl/admin/api/add-script',
            contentType: 'application/json',
            data: JSON.stringify(data.field),
            dataType: 'json',
            success: function (result) {
                if (result.code === 'success') {
                    layer.msg('保存成功', {
                        icon: 1,
                        time: 1000,
                        anim: 5
                    }, function () {
                        // 先得到当前 iframe 层的索引
                        const index = parent.layer.getFrameIndex(window.name);
                        // 再执行关闭
                        parent.layer.close(index);
                    })
                } else if (result.code === 'failure') {
                    layer.msg(result.message, {
                        icon: 2,
                        time: 2000,
                        anim: 5
                    }, function () {
                        if (result.data != null) {
                            window.location.href = result.data;
                        }
                    })
                } else {
                    layer.msg(result.message, {
                        icon: 2,
                        time: 2000,
                        anim: 5
                    })
                }
            }
        })
        return false;
    });
});