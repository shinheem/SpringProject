let isValId = false

const emailcheck = function(){
    console.log("asdasasdadadw");
    let isValidId = false
    const email = document.querySelector('#email').value
    if(!email){
        alert('아이디를 입력하세요.')
        return
    }

    const xhr = new XMLHttpRequest()
    xhr.open('GET', '/api/user/check/' + email)
    xhr.send()
    xhr.onload = function () {
        if (xhr.status === 200 || xhr.status === 201) {       //readyState가 DONOE
            const result = JSON.parse(xhr.response)
            console.log("응답", result.exist)
            //서버 응답 exist 값으로 isValidID 저장. 존재하면 새로운 회원은 사용할 수 없는 아이디
            isValid = !result.exist    //result.exist 는 true 또는 false를 리턴합니다.
            if (isValid) {
                document.querySelector('#idMessage > span').innerHTML = '사용가능한 email 입니다.'
                document.querySelector('#idMessage > span').style.color = 'green'
                // 3초 동안 보이도록 설정
                const svgElement = document.querySelector('#checksign-img');
                svgElement.classList.add('visible');
                setTimeout(function() {
                    svgElement.classList.remove('visible');
                }, 2000); // 3초 = 3000 밀리초

            }else {
                document.querySelector('#idMessage > span').innerHTML = '존재하는 email 입니다.'
                document.querySelector('#idMessage > span').style.color = 'red'
                // 3초 동안 보이도록 설정
                const svgElement = document.querySelector('#xsign-img');
                svgElement.classList.add('visible');

                setTimeout(function() {
                    svgElement.classList.remove('visible');
                }, 2000); // 3초 = 3000 밀리초
            }
        }else {
            console.error('오류', xhr.status, xhr.response)
        }



    }
    return isValId
    //비동기 함수 안에서 비동기 함수를 호출하는 것은 원하는 실행 결과를 받을 수 없음
    //실행 타이밍이 우리의 예측과 다릅니다.
}
document.querySelector('#verifyId').addEventListener('click',emailcheck)

/* function() checkid end*/


let gender  //arrSubject.toString()은 배열 요소로 문자열을 만들어줍니다. post에서 시용하기
const checkgender = e => {
    e.stopPropagation()

    const target = e.target
    if (target.tagName !== 'INPUT') {
        return
    }

    gender=target.value

    console.log(gender);
}

document.querySelector('#checkSubjects').addEventListener('click', checkgender)

//check buisness

let buisness = 'NM' //arrSubject.toString()은 배열 요소로 문자열을 만들어줍니다. post에서 시용하기
const checkMemberType = e => {
    e.stopPropagation()

    const target = e.target
    if (target.tagName !== 'INPUT') {
        return
    }

    if (target.checked){
        buisness="BO"

    }else{
        buisness="NM"
    }

    console.log(buisness);
}

document.querySelector('#checkMemberType').addEventListener('click', checkMemberType)

const save = function () {         //리터럴 형식의 함수선언 : 끌어올리기 안됨. 사용하기 전에 선언
    const email = document.querySelector('#email').value

    //id 중복확인
    let yn = false
    if (emailcheck()) {
        alert('이미 사용중인 이메일 입니다.')
        return
    } else {
        yn = confirm(`이메일 '${email}'로 가입하시겠습니까?`)
        if (!yn) return          //confirm 에서 취소누르면 false -> 함수 종료
    }


    //1. 입력값 가져오기

    const name = document.querySelector('#name').value
    const password = document.querySelector('#password').value
    const tel = document.querySelector('#tel').value
    const address = document.querySelector('#address').value
    //2. 입력값으로 자바스크립트 객체 생성(자바스크립트 객체는 미리 타입을 정의하지 않고 사용할 수 있습니다.)
    const jsObj = {  //앞의 값은 키 :  뒤의 값은 쿼리셀렉터로 불러온 id속성값
        email: email,
        name: name,
        password: password,
        tel: tel,
        address : address,
        gender: gender,
        business: buisness
    }
    console.log(jsObj)
    //3. 자바스크립트 객체를 json 전송을 위해 직렬화(문자열로 변환)
    const jsStr = JSON.stringify(jsObj)  //키에 저장한 id 속성값을 문자열로 변환 추후 자동으로 파라미터로변환
    const xhr = new XMLHttpRequest()    //비동기 통신 객체 생성
    xhr.open('POST', '/api/users')       //4. 전송 보낼 준비(url과 method)
    //Content-Type 헤더를 JSON으로 설정
    xhr.setRequestHeader("Content-type", "application/json;charset=UTF-8");
    xhr.send(jsStr)     //5. 요청 전송. POST에서는 body와 함께 보냅니다.

    xhr.onload = function () {     //요청에 대한 응답받았을 때 onload 이벤트 핸들러 함수
        console.log('*** xhr response:', xhr.response);      //선언 위치 주의하세요.
        const resultObj = JSON.parse(xhr.response);
        if (xhr.status === 200 || xhr.status === 201) {
            if (resultObj.count == 1) {
                //clear()     //정상 등록 후 입력 요소 초기화
                alert( '새로운 회원 \'' + email + '\'가입 되었습니다.')
                alert( '로그인으로 갑니다.')
                location.href = "/"
            }
        } else {
            console.log('오류1-', xhr.response)
            console.log('오류2-', xhr.status)
            const values = Object.valueOf(resultObj);   //자바스크립트 객체는 key,value 구성. 그 중에 value만 가져와서 배열로 만듭니다.
            console.log('오류메시지 :', values)
            let resultMsg = ''
            values.forEach(msg =>               //배열에 대해 실행하는 반복
                resultMsg += msg + "<br>"

            )
            document.querySelector('container').innerHTML = resultMsg

        }
    }

}

document.querySelector('#save').addEventListener('click',save)


