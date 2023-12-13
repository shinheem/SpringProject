const heart = function (){
    const xhr = new XMLHttpRequest()
    xhr.open('POST','/api/heart')
    xhr.setRequestHeader("Content-Type","application/json;charset=UTF-8");

    const idx = document.querySelector("#idx").value
    const id = document.querySelector("#email").value
    const ischecked = document.querySelector("#ischecked").checked
    const jsObj = { email:id, idx:idx, status:ischecked}
    const jsString = JSON.stringify(jsObj)
    xhr.send(jsString)
    xhr.onload = function () {
    const resultObj = JSON.parse(xhr.response);
    if (xhr.status === 200 || xhr.status === 201) {
        if(ischecked == true){
            alert('즐겨찾기가 완료되었습니다.')
        }else {
            alert('즐겨찾기가 취소되었습니다.')
        }
    } else {
        console.error('오류1-', xhr.response)
        console.error('오류2-', xhr.status)
    }
}
}
document.querySelector('#ischecked').addEventListener('click', heart)

