package qqhl.andaalarmmaster.config.hibernate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class ImprovedPhysicalNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return toUnderlineName(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return toUnderlineName(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return toUnderlineName(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return toUnderlineName(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return toUnderlineName(name);
    }

    private Identifier toUnderlineName(Identifier name) {
        if (name == null || StringUtils.isEmpty(name.getText()))
            return name;
        return Identifier.toIdentifier( camelCaseToUnderline(name.getText()) );
    }

    /* 将驼峰风格转换为下划线小写风格 */
    private static String camelCaseToUnderline(String str) {
        //TODO:需要考虑连续大写，例如URLAbc，应转换为url_abc
        StringBuilder sb = new StringBuilder();

        sb.append(Character.toLowerCase(str.charAt(0)));
        char c;
        for (int i = 1, len = str.length(); i < len; i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }

        return sb.toString();
    }
}
