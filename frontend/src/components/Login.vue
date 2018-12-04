<template>
  <div class="login">
    <form v-if="!loggedin" v-on:submit.prevent="onSubmit" >
    <div>
        <input required placeholder="citizenId" v-model="username" />
    </div>
    <div>
        <input required placeholder="password" type="password" v-model="password"/>
    </div>
        <button>login</button>
    </form>
    <div v-if="user != null">

        Welcome, {{user.firstname}}
    </div>
  </div>
</template>

<script>
import axios from 'axios'

const url = 'http://localhost:8080'

export default {
  name: 'Login',
  data() {
      return {
          username: '',
          password: '',
          loggedin: false,
          user: null
      }
  },
  methods: {
      async onSubmit() {
          console.log(this.username)
          const form = new FormData()
          form.append("username", this.username)
          form.append("password", this.password)
          axios.post(url + '/login', form)
            .then(resp => {
                const token = resp.data.token
                localStorage.setItem('x-token', token)
                this.loggedin = true
                this.getUser()
            })
            .catch(err => {
                console.error(err)
                alert('invalid username or password')
            })
      },
      getUser() {
          const token = localStorage.getItem('x-token')
          axios.get(url + '/me', {
              headers: {
                  Authorization: 'Bearer ' + token
              }
          })
            .then(resp => {
                const user = resp.data
                this.user = user
                
            })

      }
  },
  mounted() {
      console.log(this.axios)
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
