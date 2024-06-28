<template>
  <div>
    <el-card class="cardStyle" v-loading="loading">
      <el-button @click="show = !show" size="small" style="margin-bottom: 15px">筛选</el-button>
      <el-button @click="deleteByIds" type="danger" size="small" style="margin-bottom: 15px">批量删除</el-button>
      <el-button @click="handleAdd" type="info" size="small" style="margin-bottom: 15px">新增</el-button>
      <el-button @click="generatePaper" type="success" size="small" style="margin-bottom: 15px">生成试卷</el-button>
      <el-button @click="generatePaperByAnswer" type="success" size="small" style="margin-bottom: 15px">
        生成试卷(含答案)
      </el-button>

      <!--过滤框-->
      <el-collapse-transition>
        <div v-if="show" style="background-color: white;height: 100px;;box-sizing: border-box">
          <el-form ref="form" label-width="80px">
            <el-row>
              <el-col :span="8">
                <el-form-item label="题型：" prop="type">
                  <el-select v-model="type" style="width: 90%" @change="typeChange" value-key="type">
                    <el-option v-for="type in typeList" :key="type" :label="type" :value="type"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div style="margin: -5px 0px 10px 77%">
            <el-button type="primary" @click="clickAndClose" size="small" style="margin-right: 10px">确定并关闭
            </el-button>
            <el-button type="primary" @click="clickSure" size="small" style="margin-right: 10px">确定</el-button>
            <el-button size="small" @click="show = false; type = ''">取消</el-button>
          </div>
        </div>
      </el-collapse-transition>

      <el-row>
        <el-col :span="24">
          <el-table ref="questionBankTable"
                    :data="tableData"
                    :header-cell-style="{ background: '#EDF1F4' }"
                    :row-style="{}"
                    :highlight-current-row="false"
                    :stripe="false"
                    style="width: 100%"
                    :height="tableHigh"
                    @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50px" align="center">
            </el-table-column>
            <el-table-column type="index"
                             label="序号"
                             min-width="100"
                             align="center">
              <template slot-scope="scope">
                {{ startIndex + scope.$index }}
              </template>
            </el-table-column>

            <el-table-column prop="type"
                             label="题型"
                             min-width="100"
                             align="center">
            </el-table-column>
            <el-table-column prop="question"
                             label="题目"
                             min-width="100"
                             show-overflow-tooltip
                             align="center">
            </el-table-column>
            <el-table-column prop="answer"
                             label="答案"
                             min-width="100"
                             show-overflow-tooltip
                             align="center">
            </el-table-column>
            <el-table-column label="操作" min-width="50" align="center">
              <template slot-scope="scope">
                <el-row type="flex" justify="center">
                  <el-col :span="12">
                    <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                  </el-col>
                  <el-col :span="12">
                    <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                  </el-col>
                </el-row>
              </template>
            </el-table-column>
          </el-table>
          <el-row type="flex" justify="space-between" class="footer">
            <el-col>
              <div class="page">
                <!-- 添加分页组件 -->
                <el-pagination
                  background
                  :small="false"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="total">
                </el-pagination>
              </div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <!-- 编辑对话框 -->
      <el-dialog :visible.sync="editDialogVisible" title="修改题目">
        <el-form :model="editedData" label-width="80px">
          <el-form-item label="题型">
            <el-input v-model="editedData.type"></el-input>
          </el-form-item>
          <el-form-item label="题目">
            <el-input v-model="editedData.question" type="textarea" :rows="4"></el-input>
          </el-form-item>
          <el-form-item label="答案">
            <el-input v-model="editedData.answer" type="textarea" :rows="4"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEditedData">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog :visible.sync="addDialogVisible" title="新增题目">
        <el-form :model="adddData" label-width="80px">
          <el-form-item label="题型">
            <el-input v-model="adddData.type"></el-input>
          </el-form-item>
          <el-form-item label="题目">
            <el-input v-model="adddData.question" type="textarea" :rows="4"></el-input>
          </el-form-item>
          <el-form-item label="答案">
            <el-input v-model="adddData.answer" type="textarea" :rows="4"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAddData">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      currentPage: 1,
      pageSize: 10,
      total: 20, // 总条数
      editedData: {
        type: '',
        question: '',
        answer: '',
        id: '',
      },
      editDialogVisible: false, // 编辑对话框可见性
      type: '',
      startIndex: 1, // 序号的起始值
      ids: [],
      addDialogVisible: false,
      adddData: {
        type: '',
        question: '',
        answer: '',
        id: '',
      },
      show: false,
      typeList: [],
    };
  },
  methods: {
    handleSelectionChange(val) {
      // 将新的选择项追加到已有的选择项中
      this.selection = [...this.selection, ...val];
    },
    handleEdit(row) {
      // 编辑操作
      this.editedData = {...row}; // 将当前行的数据拷贝到editedData中
      this.editDialogVisible = true;
    },
    handleAdd() {
      this.addDialogVisible = true;
    },
    handleDelete(row) {
      // 删除操作
      this.$confirm('此操作将永久删除该题目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.ids.push(row.id);
        console.log(this.ids)
        this.axiosHelper.delete("/api/sms/subject", {
          params: {
            ids: this.ids.toString()
          }
        }).then(res => {
          this.$message.warning({
            message: '删除成功'
          })
          this.ids = [];
          this.getSubjectList();
        }).catch(error => {
          this.$message.error('删除失败，请稍后重试');
          this.ids = [];
        })
      }).catch(() => {
        this.$message.info({
          message: '已取消删除'
        });
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getSubjectList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.startIndex = (this.currentPage - 1) * this.pageSize + 1;
      this.getSubjectList();
    },
    saveEditedData() {
      this.loading = true;
      this.axiosHelper.put("/api/sms/subject/update", this.editedData)
        .then(res => {
          this.editDialogVisible = false
          this.$message.info({
            message: '更改成功'
          });
          this.getSubjectList();
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
          // 处理请求失败的逻辑
          this.editDialogVisible = false
          this.$message.error('保存失败，请稍后重试');
          console.error('保存失败:', error);
        })
    },
    saveAddData() {
      this.loading = true;
      this.axiosHelper.post("/api/sms/subject/add", this.adddData)
        .then(res => {
          this.addDialogVisible = false
          this.$message.info({
            message: '新增成功'
          });
          this.getSubjectList();
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
          // 处理请求失败的逻辑
          this.editDialogVisible = false
          this.$message.error('新增失败，请稍后重试');
          console.error('保存失败:', error);
        })
    },
    getSubjectList() {
      this.loading = true;
      this.axiosHelper.get("/api/sms/subject/getSubjectList", {
        params: {
          currentPage: this.currentPage,
          pageSize: this.pageSize,
          type: this.type
        }
      })
        .then(res => {
          this.loading = false;
          this.tableData = res.data.items;
          this.total = res.data.totalCount;
          this.getTypeList();
        })
        .catch(res => {
          this.loading = false;
          this.$message.error({
            message: '失败'
          })
        })
    },
    deleteByIds() {
      this.ids = this.selection.map(i => i.id);
      if (this.ids.length == 0) {
        this.$message.warning({
          message: '清先选择数据'
        })
        return;
      }
      this.axiosHelper.delete("/api/sms/subject", {
        params: {
          ids: this.ids.toString()
        }
      }).then(res => {
        this.$message.warning({
          message: '批量删除成功'
        })
        this.getSubjectList();
      }).catch(error => {
        this.$message.error('删除失败，请稍后重试');
      })
      this.ids = [];
      this.selection = []
    },
    typeChange(data) {
      this.type = data;
    },
    getTypeList() {
      this.axiosHelper.get("/api/sms/subject/getAllType")
        .then(res => {
          this.typeList = res.data;
        })
        .catch(erroe => {
          this.$message.error({
            message: '查询题型失败'
          })
        })
    },
    clickSure() {
      this.getSubjectList();
    },
    clickAndClose() {
      this.clickSure();
      this.show = false;
    },
    generatePaper() {
      if (this.selection.length === 0) {
        this.$message.info("请先选择试题");
        return;
      }
      this.ids = this.selection.map(i => i.id);
      this.axiosHelper.get("/api/sms/subject/generatePaper", {
        params: {
          withAnswer: false,
          ids: this.ids.toString()
        },
        responseType: 'blob', // 指定响应数据类型为 Blob
      }).then(res => {
        console.log("res---->", res.headers);
        const blob = new Blob([res.data]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        // 使用后端传递的文件名进行下载
        const dispositionHeader = res.headers['content-disposition'];
        const fileName = decodeURIComponent(dispositionHeader.split("filename=")[1]);
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        this.$message.success("生成成功");
      }).catch(error => {
        console.error('Error:', error);
        this.$message.error("生成失败");
      })
      this.selection = [];
      this.ids = [];
      this.getSubjectList();
    },
    generatePaperByAnswer() {
      if (this.selection.length === 0) {
        this.$message.info("请先选择试题");
        return;
      }
      this.ids = this.selection.map(i => i.id);
      this.axiosHelper.get("/api/sms/subject/generatePaper", {
        params: {
          withAnswer: true,
          ids: this.ids.toString()
        },
        responseType: 'blob', // 指定响应数据类型为 Blob
      }).then(res => {
        console.log("res---->", res.headers);
        const blob = new Blob([res.data]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        // 使用后端传递的文件名进行下载
        const dispositionHeader = res.headers['content-disposition'];
        const fileName = decodeURIComponent(dispositionHeader.split("filename=")[1]);
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        this.$message.success("生成成功");
      }).catch(error => {
        console.error('Error:', error);
        this.$message.error("生成失败");
      })
      this.selection = [];
      this.ids = [];
      this.getSubjectList();
    }
  },
  computed: {
    tableHigh() {
      return this.show ? '52vh' : '66vh'
    }
  },
  mounted() {
    this.getSubjectList();
  }
};
</script>


<style scoped>
.cardStyle {
  height: 78vh;
  margin: 10px;
  padding: 15px 10px 10px 10px;
}
</style>
