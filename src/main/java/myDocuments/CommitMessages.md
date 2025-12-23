## 1.提交的基本格式
```python
<type>(<scope>): <subject>

<body>

<footer>
```
## 2.详细规范

### Type（必需）- 提交类型

| 类型 | 描述 |
|---|--|
| feat | 新功能（feature） |
| fix | 修复bug |
|  docs |文档更新 |
|  style |代码格式调整（不影响代码逻辑）|
|refactor|重构（既不是新功能，也不是bug修复）|
|perf|性能优化|
|test|测试相关|
|chore|构建过程或辅助工具的变动|
|ci|CI配置或脚本修改|
|build|构建系统或外部依赖的更改|
|revert|回滚之前的提交|

### Scope（可选）- 影响范围

模块名、组件名、文件名等

例：feat(auth):、fix(api):、docs(readme):

### Subject（必需）- 简短描述
50个字符以内

使用祈使句（动词开头）

不要以大写字母开头

不要以句号结尾

例："add"而不是"added"，"fix"而不是"fixed"

## 完整示例
```text
feat(payment): 集成支付宝支付接口

- 添加AlipayService实现
- 配置支付回调处理器
- 更新支付文档

Closes #123
BREAKING CHANGE: 支付接口返回格式变更，详见migration.md

```

## 最佳实践清单
✅ 每次提交只做一件事（单一职责）

✅ 提交前运行测试

✅ 关联问题追踪号

✅ 如果是破坏性变更，必须明确标出

✅ 保持提交历史线性整洁（使用rebase）

✅ 团队统一标准

✅ 定期清理提交历史（交互式rebase）
