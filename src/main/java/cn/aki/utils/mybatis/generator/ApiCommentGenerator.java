package cn.aki.utils.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Optional;
import java.util.Set;

/**
 * @author llh
 * Created on 2018/10/8.
 */
public class ApiCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addAnnotation("@ApiModel");
        topLevelClass.addImportedType("javax.persistence.GeneratedValue");
        topLevelClass.addImportedType("javax.persistence.GenerationType");
        topLevelClass.addImportedType("javax.persistence.Id");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 注释
        field.addJavaDocLine("/**"); //$NON-NLS-1$
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));  //$NON-NLS-1$
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" *   " + remarkLine);  //$NON-NLS-1$
                field.addAnnotation("@ApiModelProperty(\"" + remarkLine + "\")");
            }
        }
        field.addJavaDocLine(" */"); //$NON-NLS-1$
        // 主键注解
        String pk = Optional.of(introspectedTable)
                .map(IntrospectedTable::getGeneratedKey)
                .map(GeneratedKey::getColumn)
                .orElse(null);
        if (introspectedColumn.getActualColumnName().equals(pk)) {
            field.addAnnotation("@Id");
            field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
        }
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
        super.addFieldAnnotation(field, introspectedTable, introspectedColumn, imports);
    }
}
