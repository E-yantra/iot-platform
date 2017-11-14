
const Login = {template: '#login-template'};
const Signup = {template: '#signup-template'};
const Dashboard = {template: ''};
const Sidebar = {template: '#sidebar-template'};


const routes = [
    {path: '/', component:Login},
    {path: '/login', component: Login},
    {path: '/signup', component: Signup},
    {path: '/dashboard', component: Dashboard}
];


const router = new VueRouter({
    routes // short for `routes: routes`
});

if(!$.cookie("auth_token")){
    router.replace("/login");
}

Vue.component('sidebar',Sidebar);

const app = new Vue({
    router
}).$mount('#container-main');