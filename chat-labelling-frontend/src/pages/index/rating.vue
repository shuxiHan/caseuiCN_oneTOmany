<template>
  <div style="width:800px; margin: 100px auto">
    <h1 v-if="role === 'cus' && recommend_info.length !== 0" style="text-align: center">产品相关性评价</h1>
    <p v-if="role === 'cus' && recommend_info.length !== 0" style="text-align: center; font-size: 14px; margin-bottom: 20px;">
      请评估系统推荐的产品是否符合您的需求（是否相关）
    </p>
    <h1  v-if="role === 'sys'" style="text-align:center">请评价这次对话</h1>
    <Divider />
    <!-- 显示搜索结果的部分 -->
<!--    <Button type="primary" @click="fetchSearchedMessage">显示</Button>-->
<!--    <div v-if="searchedMessage">-->
<!--      <h3>Search Result:</h3>-->
<!--      <p>{{ searchedMessage }}</p>-->
<!--    </div>-->
    <div v-for="(item, index) in recommend_info" :key="index">
      <h3>{{ item.name }}</h3>
      <RadioGroup v-model="selectedOptions[index]">
        <Radio :label="1">相关</Radio>
        <Radio :label="0">不相关</Radio>
      </RadioGroup>
      <br><br>
    </div>
    <div v-if="role === 'cus'">
      <h1 style="text-align:center">请评价这次对话</h1>
      <h3>您认为系统是否成功向您推荐了您的目标产品？</h3>
      <RadioGroup v-model="cus_rate.anotheranswer">
        <Radio label="yes">是</Radio>
        <Radio label="no">否</Radio>
      </RadioGroup>
      <br><br>
      <h3 slot="label">您认为您的伙伴表现如何？</h3>
      <Rate allow-half v-model="cus_rate.rate" />
      <br><br>
    </div>
    <!--    <button @click="saveResults">Save Results</button>-->
    <Form :model="formItem" ref="form" label-position="top" :rules="rules" :show-message="false">
<!--      <FormItem prop="goalAchieve" v-if="role==='cus'">-->
<!--        <h3 slot="label">Did you achieve your goal?</h3>-->
<!--        <RadioGroup v-model="formItem.goalAchieve">-->
<!--          <Radio label=1>related</Radio>-->
<!--          <Radio label=0>Unrelated</Radio>-->
<!--        </RadioGroup>-->
<!--      </FormItem>-->
      <FormItem prop="goalUnderstand" v-if="role==='sys'">
        <h3 slot="label">您认为您理解了用户的目标产品意图吗？</h3>
        <RadioGroup v-model="formItem.goalUnderstand">
          <Radio label=1>是</Radio>
          <Radio label=0>否</Radio>
        </RadioGroup>
      </FormItem>
<!--      <FormItem prop="conversationSearch" v-if="role==='cus'">-->
<!--        <h3 slot="label">Would you like to conduct search through conversations in this way?</h3>-->
<!--        <RadioGroup v-model="formItem.conversationSearch">-->
<!--          <Radio label=1>related</Radio>-->
<!--          <Radio label=0>Unrelated</Radio>-->
<!--        </RadioGroup>-->
<!--      </FormItem>-->
      <FormItem prop="easierWay" v-if="role==='sys'">
        <h3 slot="label">哪个选项更容易让用户实现他/她的目标？</h3>
        <RadioGroup v-model="formItem.easierWay">
          <Radio label=1>搜索</Radio>
          <Radio label=0>对话</Radio>
        </RadioGroup>
      </FormItem>
      <FormItem prop="rate" v-if="role==='sys'">
        <h3 slot="label">您认为您的伙伴的表现如何？</h3>
        <Rate allow-half v-model="formItem.rate" />
      </FormItem>
      <FormItem v-if="role==='cus'">
        <Button type="primary" @click="restart">提交</Button>
        <!-- <Button @click="logout">Submit and Logout</Button> -->
        <!-- <Button @click="close">Submit and Close</Button> -->
      </FormItem>
      <FormItem v-if="role==='sys'">
        <Button type="primary" @click="restartSys">提交</Button>
        <!-- <Button @click="logout">Submit and Logout</Button> -->
        <!-- <Button @click="close">Submit and Close</Button> -->
      </FormItem>
    </Form>
  </div>
</template>
<script>
export default {
  props: ['role', 'conversationId', 'userId', 'recommend_info'],
  data () {
    return {
      extractedTextList: [],
      cus_rate: {
        anotheranswer: null,
        rate: 0
      },
      searchedMessage: null,
      // recommend_info: [],
      selectedOptions: [],
      formItem: {
        goalAchieve: 0,
        conversationSearch: 0,
        rate: 0,
        goalUnderstand: 0,
        easierWay: 0
      },
      rules: {
        goalAchieve: [ { trigger: 'change' } ],
        conversationSearch: [ { message: 'Please select ', trigger: 'change' } ],
        rate: [ {trigger: 'change',
          validator: (rule, value, callback) => {
            if (!value) {
              return callback(new Error('Please rate your partner!'))
            } else callback()
          }} ],
        goalUnderstand: [ { trigger: 'change' } ],
        easierWay: [ { trigger: 'change' } ]
      }
    }
  },
  methods: {
    restart () {
      // console.log(this.recommend_info.length)
      if (this.selectedOptions.length === this.recommend_info.length && this.cus_rate.anotheranswer != null) {
        this.$refs.form.validate((valid) => {
          if (!valid) {
            this.$Message.error('Please fill in the form!')
            return
          }
          this.$http.post('/api/saveRating', {
            recommend_info: this.recommend_info,
            selectedOptions: this.selectedOptions,
            role: this.role,
            userId: this.userId,
            conversationId: this.conversationId
          }).then((response) => {
            // this.$emit('completed')
          }).catch((e) => {
            this.submitting = false
            this.$Message.error('Something wrong when sending data!')
          })
          this.$http.post('/api/saveRatingSys', {...this.cus_rate, role: this.role, conversationId: this.conversationId}).then((response) => {
            this.$emit('completed')
          }).catch((e) => {
            this.submitting = false
            this.$Message.error('Something wrong when sending data!')
          })
        })
      } else {
        this.$Message.error('Please fill in the form!')
      }
    },
    restartSys () {
      this.$refs.form.validate((valid) => {
        if (!valid) {
          this.$Message.error('Please fill in the form!')
          return
        }
        this.$http.post('/api/saveRatingSys', {...this.formItem, role: this.role, conversationId: this.conversationId}).then((response) => {
          this.$emit('completed')
        }).catch((e) => {
          this.submitting = false
          this.$Message.error('Something wrong when sending data!')
        })
      })
    },
    logout () {
      this.$refs.form.validate((valid) => {
        if (!valid) {
          this.$Message.error('Please fill in the form!')
          return
        }
        this.$http.post('/api/saveRating', {...this.formItem, role: this.role, userId: this.userId, conversationId: this.conversationId}).then((response) => {
          window.location = '/logout'
        }).catch((e) => {
          this.submitting = false
          this.$Message.error('Something wrong when sending data!')
        })
      })
    }
  }
}
</script>
