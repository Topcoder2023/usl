package com.gitee.usl.resource.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Notes;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.utils.AnnotatedComparator;
import com.gitee.usl.infra.utils.NumberWrapper;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.resource.Returns;
import com.gitee.usl.resource.TreeInfo;
import com.gitee.usl.resource.api.WebHandler;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class InitializerHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/initializer";
    private List<TreeInfo> infoCache;

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        this.writeToJson(Returns.success(this.getInfoCache()));
    }

    public List<TreeInfo> getInfoCache() {
        if (infoCache != null) {
            return infoCache;
        }

        List<Initializer> initializers = ServiceSearcher.searchAll(Initializer.class);

        NumberWrapper.IntWrapper wrapper = NumberWrapper.ofIntWrapper();

        this.infoCache = initializers.stream()
                .map(item -> {
                    Class<? extends Initializer> itemClass = item.getClass();

                    TreeInfo info = new TreeInfo();
                    info.setId(wrapper.incrementAndGet());
                    Notes notes = AnnotationUtil.getAnnotation(itemClass, Notes.class);
                    if (notes == null) {
                        info.setName(itemClass.getSimpleName());
                        info.setBelongs(ModuleConstant.DEFAULT);
                        info.setViewUrl(CharSequenceUtil.EMPTY);
                    } else {
                        info.setName(notes.value());
                        info.setBelongs(notes.belongs());
                        info.setViewUrl(notes.viewUrl());
                    }
                    info.setOrder(AnnotatedComparator.getOrder(itemClass));
                    info.setClassName(itemClass.getName());
                    info.setByUsl(ModuleConstant.isFrom(info.getBelongs()));

                    return info;
                })
                .collect(Collectors.toList());

        this.infoCache.stream()
                .collect(Collectors.groupingBy(TreeInfo::getBelongs))
                .forEach((key, value) -> {
                    TreeInfo parent = new TreeInfo();
                    parent.setId(wrapper.incrementAndGet());
                    parent.setParentId(NumberConstant.MINUS_ONE);
                    parent.setName(key);
                    parent.setByUsl(Boolean.TRUE);
                    parent.setBelongs(key);
                    this.infoCache.add(parent);

                    value.forEach(child -> child.setParentId(parent.getId()));
                });

        return infoCache;
    }
}
