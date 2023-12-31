package com.googlecode.aviator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.googlecode.aviator.exception.ExpressionNotFoundException;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.lexer.token.Variable;
import com.googlecode.aviator.parser.CompileTypes;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;
import com.googlecode.aviator.runtime.function.LambdaFunction;
import com.googlecode.aviator.utils.Constants;
import com.googlecode.aviator.utils.Env;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hongda.li
 */
public abstract class BaseExpression implements Expression {
    private static final long serialVersionUID = 2819544277750883372L;

    public static final String FUNC_PARAMS_VAR = "__funcs_args__";
    protected List<String> varNames;
    protected List<String> varFullNames;

    @Getter
    private List<VariableMeta> vars;

    @Getter
    @Setter
    private String expression;

    @Setter
    protected transient AviatorEvaluatorInstance instance;

    @Getter
    private Env compileEnv;

    private Map<Integer, List<FunctionArgument>> funcsArgs = Collections.emptyMap();
    @Getter
    protected SymbolTable symbolTable;

    @Setter
    protected String sourceFile;

    @Getter
    @Setter
    protected Map<String, LambdaFunctionBootstrap> lambdaBootstraps;

    @Override
    public String getSourceFile() {
        return this.sourceFile;
    }

    public BaseExpression(final AviatorEvaluatorInstance instance,
                          final List<VariableMeta> vars,
                          final SymbolTable symbolTable) {
        super();
        this.vars = vars;
        this.symbolTable = symbolTable;
        this.instance = instance;
    }

    private void populateNames() {
        if (this.varNames == null) {
            if (this.varFullNames == null) {
                populateFullNames();
            }

            List<String> newVarNames = new ArrayList<>(this.varFullNames.size());
            Set<String> nameSet = new HashSet<>();
            Set<String> parentInitNames = new HashSet<>();

            for (VariableMeta m : this.vars) {
                if (m.isInit() && !m.getName().contains(".") && m.getFirstIndex() >= 0) {
                    parentInitNames.add(m.getName());
                }
            }

            for (String fName : this.varFullNames) {
                String[] tmps = Constants.SPLIT_PAT.split(fName);
                String sName = tmps[0];
                if (!nameSet.contains(sName) && !parentInitNames.contains(sName)) {
                    newVarNames.add(sName);
                    nameSet.add(sName);
                }
            }
            this.varNames = newVarNames;
        }
    }

    protected void afterPopulateFullNames(final Map<String, VariableMeta> fullNames,
                                          final Set<String> parentVars) {
        if (this.lambdaBootstraps != null && !this.lambdaBootstraps.isEmpty()) {
            for (LambdaFunctionBootstrap bootstrap : this.lambdaBootstraps.values()) {
                for (VariableMeta meta : bootstrap.getClosureOverFullVarNames()) {
                    VariableMeta existsMeta = fullNames.get(meta.getName());
                    if (existsMeta == null) {
                        if (!parentVars.contains(meta.getName())) {
                            fullNames.put(meta.getName(), meta);
                        }
                    } else {
                        // Appear first, update the meta
                        if (existsMeta.getFirstIndex() > meta.getFirstIndex()) {
                            fullNames.put(meta.getName(), meta);
                        }
                    }
                }
            }
        }
    }

    private void populateFullNames() {
        if (this.varFullNames == null) {
            Map<String, VariableMeta> fullNames = getFullNameMetas();

            final ArrayList<VariableMeta> metas = new ArrayList<>(fullNames.values());
            Collections.sort(metas, new Comparator<VariableMeta>() {

                @Override
                public int compare(final VariableMeta o1, final VariableMeta o2) {
                    return Integer.compare(o1.getFirstIndex(), o2.getFirstIndex());
                }

            });

            List<String> newFullNames = new ArrayList<>(fullNames.size());
            for (VariableMeta meta : metas) {
                newFullNames.add(meta.getName());
            }

            this.varFullNames = newFullNames;
        }
    }

    public Map<String, VariableMeta> getFullNameMetas() {
        Map<String, VariableMeta> fullNames = new LinkedHashMap<>(this.vars.size());
        Set<String> parentVars = new HashSet<>(this.vars.size());
        Set<String> definedVars = new HashSet<>();

        for (VariableMeta m : this.vars) {
            final String name = m.getName();
            String[] tmps = Constants.SPLIT_PAT.split(name);
            if (!m.isInit() && m.getType() != CompileTypes.Class && !definedVars.contains(tmps[0])
                    && !definedVars.contains(name) && m.getFirstIndex() >= 0) {
                fullNames.put(name, m);
            } else if (m.getFirstIndex() >= 0) {
                // It's defined in current scope
                definedVars.add(name);
                definedVars.add(tmps[0]);
            }
            parentVars.add(name);
        }

        afterPopulateFullNames(fullNames, parentVars);
        return fullNames;
    }

    private class SymbolHashMap extends HashMap<String, Object> {

        private static final long serialVersionUID = 5951510458689965590L;

        public SymbolHashMap(final int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public Object put(String key, final Object value) {
            Variable var = null;
            if (BaseExpression.this.symbolTable != null
                    && (var = BaseExpression.this.symbolTable.getVariable(key)) != null) {
                key = var.getLexeme();
            }
            return super.put(key, value);
        }

    }

    @Override
    public Map<String, Object> newEnv(final Object... args) {
        if (args != null && args.length % 2 != 0) {
            throw new IllegalArgumentException("Expect arguments number is even.");
        }
        Map<String, Object> env = new SymbolHashMap(args != null ? args.length : 10);
        if (args != null) {
            for (int i = 0; i < args.length; i += 2) {
                String key = (String) args[i];
                env.put(key, args[i + 1]);
            }
        }
        return env;
    }

    public abstract Object executeDirectly(final Map<String, Object> env);

    @Override
    public Object execute(Map<String, Object> map) {
        if (map == null) {
            map = Collections.emptyMap();
        }
        Env env = genTopEnv(map);
        EnvProcessor envProcessor = this.instance.getEnvProcessor();
        if (envProcessor != null) {
            envProcessor.beforeExecute(env, this);
        }
        try {
            return executeDirectly(env);
        } finally {
            if (envProcessor != null) {
                envProcessor.afterExecute(env, this);
            }
        }
    }

    public void setFuncsArgs(final Map<Integer, List<FunctionArgument>> funcsArgs) {
        if (funcsArgs != null) {
            this.funcsArgs = Collections.unmodifiableMap(funcsArgs);
        }
    }

    public void setCompileEnv(final Env compileEnv) {
        this.compileEnv = compileEnv;
        this.compileEnv.setExpression(this);
    }

    @Override
    public String addSymbol(final String name) {
        if (this.symbolTable != null) {
            return this.symbolTable.reserve(name).getLexeme();
        } else {
            return name;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.IExpression#execute()
     */
    @Override
    public Object execute() {
        return this.execute(null);
    }

    @Override
    public List<String> getVariableFullNames() {
        populateFullNames();
        return this.varFullNames;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.IExpression#getVariableNames()
     */
    @Override
    public List<String> getVariableNames() {
        populateNames();
        return this.varNames;
    }

    protected Env newEnv(final Map<String, Object> map, final boolean direct) {
        Env env;
        if (direct) {
            env = new Env(map, map == Collections.EMPTY_MAP ? new HashMap<String, Object>() : map);
        } else {
            env = new Env(map);
        }
        env.configure(this.instance, this);
        return env;
    }

    protected Env genTopEnv(final Map<String, Object> map) {
        if (map instanceof Env) {
            ((Env) map).configure(this.instance, this);
        }
        Env env =
                newEnv(map, this.instance.getOptionValue(Options.USE_USER_ENV_AS_TOP_ENV_DIRECTLY).bool);

        if (this.compileEnv != null && !this.compileEnv.isEmpty()) {
            env.putAll(this.compileEnv);
        }
        if (!this.funcsArgs.isEmpty()) {
            env.override(FUNC_PARAMS_VAR, this.funcsArgs);
        }
        return env;
    }

    protected Env newEnv(final Map<String, Object> map) {
        return newEnv(map, false);
    }

    public LambdaFunction newLambda(final Env env, final String name) {
        LambdaFunctionBootstrap bootstrap = this.lambdaBootstraps.get(name);
        if (bootstrap == null) {
            throw new ExpressionNotFoundException("Lambda " + name + " not found");
        }
        return bootstrap.newInstance(env);
    }

    @SuppressWarnings("unchecked")
    public void customReadObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
        this.vars = (List<VariableMeta>) input.readObject();
        this.expression = (String) input.readObject();
        this.compileEnv = (Env) input.readObject();
        this.funcsArgs = (Map<Integer, List<FunctionArgument>>) input.readObject();
        this.symbolTable = (SymbolTable) input.readObject();
        this.sourceFile = (String) input.readObject();
        this.lambdaBootstraps = (Map<String, LambdaFunctionBootstrap>) input.readObject();
    }

    public void customWriteObject(ObjectOutputStream output) throws IOException {
        output.writeObject(this.vars);
        output.writeObject(this.expression);
        output.writeObject(this.compileEnv);
        output.writeObject(this.funcsArgs);
        output.writeObject(this.symbolTable);
        output.writeObject(this.sourceFile);
        output.writeObject(this.lambdaBootstraps);
    }

}
