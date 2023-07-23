package me.phoenixra.atumodcore.json.jsonpath.internal.filter;

import me.phoenixra.atumodcore.json.jsonpath.InvalidPathException;
import me.phoenixra.atumodcore.json.jsonpath.JsonPathException;
import me.phoenixra.atumodcore.json.jsonpath.Predicate;
import me.phoenixra.atumodcore.json.jsonpath.internal.Path;
import me.phoenixra.atumodcore.json.jsonpath.internal.path.PathCompiler;
import me.phoenixra.atumodcore.json.minidev.json.parser.JSONParser;

import java.time.OffsetDateTime;
import java.util.regex.Pattern;

public abstract class ValueNode {

    public abstract Class<?> type(Predicate.PredicateContext ctx);

    public boolean isPatternNode() {
        return false;
    }

    public ValueNodes.PatternNode asPatternNode() {
        throw new InvalidPathException("Expected regexp node");
    }

    public boolean isPathNode() {
        return false;
    }

    public ValueNodes.PathNode asPathNode() {
        throw new InvalidPathException("Expected path node");
    }

    public boolean isNumberNode() {
        return false;
    }

    public ValueNodes.NumberNode asNumberNode() {
        throw new InvalidPathException("Expected number node");
    }

    public boolean isStringNode() {
        return false;
    }

    public ValueNodes.StringNode asStringNode() {
        throw new InvalidPathException("Expected string node");
    }

    public boolean isBooleanNode() {
        return false;
    }

    public ValueNodes.BooleanNode asBooleanNode() {
        throw new InvalidPathException("Expected boolean node");
    }

    public boolean isJsonNode() {
        return false;
    }

    public ValueNodes.JsonNode asJsonNode() {
        throw new InvalidPathException("Expected json node");
    }

    public boolean isPredicateNode() {
        return false;
    }

    public ValueNodes.PredicateNode asPredicateNode() {
        throw new InvalidPathException("Expected predicate node");
    }

    public boolean isValueListNode() {
        return false;
    }

    public ValueNodes.ValueListNode asValueListNode() {
        throw new InvalidPathException("Expected value list node");
    }

    public boolean isNullNode() {
        return false;
    }

    public ValueNodes.NullNode asNullNode() {
        throw new InvalidPathException("Expected null node");
    }

    public ValueNodes.UndefinedNode asUndefinedNode() {
        throw new InvalidPathException("Expected undefined node");
    }

    public boolean isUndefinedNode() {
        return false;
    }

    public boolean isClassNode() {
        return false;
    }

    public ValueNodes.ClassNode asClassNode() {
        throw new InvalidPathException("Expected class node");
    }

    //workaround for issue: https://github.com/json-path/JsonPath/issues/613
    public boolean isOffsetDateTimeNode(){
        return false;
    }

    public ValueNodes.OffsetDateTimeNode asOffsetDateTimeNode(){
        throw new InvalidPathException("Expected offsetDateTime node");
    }


    private static boolean isPath(Object o) {
        if(o == null || !(o instanceof String)){
            return false;
        }
        String str = o.toString().trim();
        if (str.length() <= 0) {
            return false;
        }
        char c0 = str.charAt(0);
        if(c0 == '@' || c0 == '$'){
            try {
                PathCompiler.compile(str);
                return true;
            } catch(Exception e){
                return false;
            }
        }
        return false;
    }

    private static boolean isJson(Object o) {
        if(o == null || !(o instanceof String)){
            return false;
        }
        String str = o.toString().trim();
        if (str.length() <= 1) {
            return false;
        }
        char c0 = str.charAt(0);
        char c1 = str.charAt(str.length() - 1);
        if ((c0 == '[' && c1 == ']') || (c0 == '{' && c1 == '}')){
            try {
                new JSONParser(JSONParser.MODE_PERMISSIVE).parse(str);
                return true;
            } catch(Exception e){
                return false;
            }
        }
        return false;
    }



    //----------------------------------------------------
    //
    // Factory methods
    //
    //----------------------------------------------------
    public static ValueNode toValueNode(Object o){

        if(o == null) return ValueNodes.NULL_NODE;
        if(o instanceof ValueNode) return (ValueNode)o;
        if(o instanceof Class) return createClassNode((Class)o);
        else if(isPath(o)) return new ValueNodes.PathNode(o.toString(), false, false);
        else if(isJson(o)) return createJsonNode(o.toString());
        else if(o instanceof String) return createStringNode(o.toString(), true);
        else if(o instanceof Character) return createStringNode(o.toString(), false);
        else if(o instanceof Number) return createNumberNode(o.toString());
        else if(o instanceof Boolean) return createBooleanNode(o.toString());
        else if(o instanceof Pattern) return createPatternNode((Pattern)o);
        else if (o instanceof OffsetDateTime) return createOffsetDateTimeNode(o.toString());  //workaround for issue: https://github.com/json-path/JsonPath/issues/613
        else throw new JsonPathException("Could not determine value type");

    }

    public static ValueNodes.StringNode createStringNode(CharSequence charSequence, boolean escape){
        return new ValueNodes.StringNode(charSequence, escape);
    }

    public static ValueNodes.ClassNode createClassNode(Class<?> clazz){
        return new ValueNodes.ClassNode(clazz);
    }

    public static ValueNodes.NumberNode createNumberNode(CharSequence charSequence){
        return new ValueNodes.NumberNode(charSequence);
    }

    public static ValueNodes.BooleanNode createBooleanNode(CharSequence charSequence){
        return Boolean.parseBoolean(charSequence.toString()) ? ValueNodes.TRUE : ValueNodes.FALSE;
    }

    public static ValueNodes.NullNode createNullNode(){
        return ValueNodes.NULL_NODE;
    }

    public static ValueNodes.JsonNode createJsonNode(CharSequence json) {
        return new ValueNodes.JsonNode(json);
    }

    public static ValueNodes.JsonNode createJsonNode(Object parsedJson) {
        return new ValueNodes.JsonNode(parsedJson);
    }

    public static ValueNodes.PatternNode createPatternNode(CharSequence pattern) {
        return new ValueNodes.PatternNode(pattern);
    }

    public static ValueNodes.PatternNode createPatternNode(Pattern pattern) {
        return new ValueNodes.PatternNode(pattern);
    }

    //workaround for issue: https://github.com/json-path/JsonPath/issues/613
    public static ValueNodes.OffsetDateTimeNode createOffsetDateTimeNode(CharSequence charSequence){
        return new ValueNodes.OffsetDateTimeNode(charSequence);
    }


    public static ValueNodes.UndefinedNode createUndefinedNode() {
        return ValueNodes.UNDEFINED;
    }

    public static ValueNodes.PathNode createPathNode(CharSequence path, boolean existsCheck, boolean shouldExists) {
        return new ValueNodes.PathNode(path, existsCheck, shouldExists);
    }

    public static ValueNode createPathNode(Path path) {
        return new ValueNodes.PathNode(path);
    }


}

