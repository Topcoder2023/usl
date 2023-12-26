package com.gitee.usl.infra.structure;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface Context<C> {

    C context();
}
