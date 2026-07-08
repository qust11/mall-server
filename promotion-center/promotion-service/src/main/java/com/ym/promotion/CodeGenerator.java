package com.ym.promotion;


import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Scanner;

/**
 * @author qushutao
 * @since 2026-06-18 21:45
 **/
public class CodeGenerator {

    private static final String URL = "jdbc:mysql://localhost:3306/mall_promotion?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static final String module = "promotion";

    private static final String projectPath = "D:\\code\\java\\mall\\promotion-center\\promotion-service";
    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author("qushutao") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(projectPath + "/src/main/java") //文件输出路径
                            .disableOpenDir(); // 指定输出目录
                })
//                .dataSourceConfig(builder ->
//                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
//                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
//                            if (typeCode == Types.SMALLINT) {
//                                // 自定义类型转换
//                                return DbColumnType.INTEGER;
//                            }
//                            return typeRegistry.getColumnType(metaInfo);
//                        })
//                )
                .packageConfig(builder ->
                        builder.parent("com.ym") // 设置父包名
                                .moduleName(module)
                                .controller("controller." + module)
                                .service("service." + module)
                                .serviceImpl("service." + module + ".impl")
                                .mapper("mapper." + module)
                                .entity("entity." + module)
                                .pathInfo(Collections.singletonMap(OutputFile.xml,  "src/main/resources/mappers")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude(scanner("表名")) // 设置需要生成的表名
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "（多个表用 , 分割）：");
        System.out.println(help);
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
